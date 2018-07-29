package com.example.combinators

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}

import scala.annotation.tailrec
import scala.collection.JavaConverters
import java.util.concurrent.{ConcurrentHashMap, TimeoutException}

import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask

import scala.collection.mutable.ListBuffer

/** Signal to start parallel generation. */
case object Start

/** Signal that parallel generation completed. */
case class WorkDone[T](resultMap: ConcurrentHashMap[(Int, Int), List[List[T]]])

/** Signal to start generating combinations on particular interval. */
final case class Task[T](limits: (Int, Int), alphabet: List[T])

/** Signal that particular task completed. */
final case class TaskResult[T](limits: (Int, Int), list: List[List[T]])

/** Object to create [[ActorManager]]. */
object ActorManager {
  def props[T](tasks: List[(Int, Int)], alphabet: List[T], actors: List[ActorRef]): Props = {
    Props(new ActorManager(tasks, alphabet, actors))
  }
}

/** Orchestrates tasks of generating combinations. */
class ActorManager[T](tasks: List[(Int, Int)], alphabet: List[T], actors: List[ActorRef]) extends Actor {
  // actually might be just a map since only one thread being executed at receiving event
  val resultMap = new ConcurrentHashMap[(Int, Int), List[List[T]]]()
  val taskList: ListBuffer[(Int, Int)] = tasks.to[ListBuffer]
  val availableActors: ListBuffer[ActorRef] = actors.to[ListBuffer]
  var mainThread: ActorRef = sender

  def receive = {
    case Start => {
      println("---- ActionManager started ----")
      // storing link to main thread
      mainThread = sender
      (0 to actors.length - 1).toList.foreach(i => {
        val actor = availableActors.remove(0)
        val task = taskList.remove(0)
        actor ! Task(task, alphabet)
      })
    }
    case TaskResult(limits, seqs) => {
      println(s"---- got calculated sequences at interval: ${limits}")
      resultMap.put(limits, seqs.asInstanceOf[List[List[T]]])
      if (taskList.isEmpty) {
        availableActors += sender
        sender ! PoisonPill
        if (availableActors.length == actors.length) {
          // last task completed
          mainThread ! WorkDone(resultMap)
        }
      } else {
        val task = taskList.remove(0)
        sender ! Task(task, alphabet)
      }
    }
  }

  override def aroundPostStop(): Unit = {
    println("ActorManager stopped")
  }
}

/** Object to create [[Worker]]. */
object Worker {
  def props: Props = Props[Worker]
}

/** Worker for generating combinations on passed interval. */
class Worker extends Actor {

  import SequenceGenerator._

  def receive = {
    case Task((start, end), alphabet) => {
      val res = generateCombsOnInterval(calculateShift(alphabet, start), calculateShift(alphabet, end), alphabet)
      sender ! TaskResult((start, end), res)
    }
  }

  override def aroundPostStop(): Unit = {
    println(s"${self.path.name} stopped")
  }
}

/** Generates sequences from given alphabet. Can be thought of as "brute force" algorithm. */
object SequenceGenerator {

  /**
    * Generates sequences up to the specified length from given alphabet.
    * "chunksNumber" defines numbers of chunks to split source works into.
    * For example, if alphabet equals [1, 2] and size = 2 then the output equals:
    * List(1),
    * List(2),
    * List(1, 1),
    * List(2, 1),
    * List(1, 2),
    * List(2, 2)
    *
    * */
  def seqCombinations[T](alphabet: List[T], size: Int, threadCount: Int, chunksNumber: Int, timeout: Timeout):
  List[List[T]] = {

    @tailrec
    def _compareCombs(xs: List[T], ys: List[T]): Boolean = {
      if (xs == Nil && ys == Nil) true
      else if (xs.length != ys.length) xs.length < ys.length
      else if (xs.last != ys.last) alphabet.indexOf(xs.last) < alphabet.indexOf(ys.last)
      else _compareCombs(xs.take(xs.length - 1), ys.take(ys.length - 1))
    }

    val init = List(alphabet.head)
    val end = List.fill(size)(alphabet.last)
    if (init == end) return List(init)
    val combsCount = combinationsCount(end, alphabet)
    val partsNumber = (combsCount / chunksNumber.toDouble).toInt
    if (partsNumber == 0) {
      println("given chunksNumber is too high for the collection. Going to use default strategy")
      return init :: generateCombsOnInterval(init, List.fill(size)(alphabet.last), alphabet)
    }

    val partsList = ((1 to combsCount by partsNumber).takeWhile(_ <= combsCount) :+ combsCount).distinct.toList
    val taskList = (0 to partsList.length - 2).map(i => (partsList(i), partsList(i + 1))).toList
    val actualThreadCount = if (taskList.length < threadCount) taskList.length else threadCount
    if (actualThreadCount != threadCount) {
      println(s"number of threads is bigger than resulted number of intervals. " +
        s"Going to reduce number of threads to ${actualThreadCount}")
    }
    val resultMap = _generateSequencesConcurrently(taskList, alphabet, actualThreadCount)(timeout)
    val sortedKeys = JavaConverters.asScalaIterator(resultMap.keySet().iterator()).toList.sortBy(t => t._1)
    println(s"------------ RESULT ------------")
    init :: sortedKeys.map(t => resultMap.get(t)).flatten
  }

  /**
    * Generates set of combinations with given alphabet on interval from "end" to "start" not including "start".
    * For example, for start = List(1, 2) and end = List(2, 1, 1) with alphabet = List(1, 2) the result equals:
    * List(2, 2)
    * List(1, 1, 1)
    * List(2, 1, 1)
    **/
  def generateCombsOnInterval[T](start: List[T], end: List[T], alphabet: List[T]): List[List[T]] = {

    @tailrec
    def _generateCombsOnInterval(start: List[T],
                                 end: List[T], alphabet: List[T], result: List[List[T]]): List[List[T]] = {
      if (start == end) result
      else _generateCombsOnInterval(start, decrement(end, alphabet), alphabet, end :: result)
    }

    _generateCombsOnInterval(start, decrement(end, alphabet), alphabet, List(end))
  }

  private def decrement[T](list: List[T], alphabet: List[T]): List[T] = {

    def _dec(list: List[T], alphabet: List[T]): List[T] = list match {
      case Nil => Nil
      case x :: xs => {
        if (x == alphabet.head) alphabet.last :: decrement(xs, alphabet)
        else alphabet(alphabet.indexOf(x) - 1) :: xs
      }
    }

    list match {
      case Nil => Nil
      case x :: xs => {
        if (list.forall(_ == alphabet.head)) List.fill(list.length - 1)(alphabet.last) else _dec(list, alphabet)
      }
    }
  }

  /**
    * Calculates amount of previous sequential shifts to reach given "source" state for given alphabet.
    * For example, for source [1, 2] and alphabet [1, 2, 3] shift equals 7 because it requires 7 sequences:
    * List(1), List(2), List(3), List(1, 1), List(2, 1), List(3, 1), List(1, 2)
    * */
  def combinationsCount[T](source: List[T], alphabet: List[T]): Int = {
    def _combinationsCount(source: List[T], alphabet: List[T], index: Int): Int = {
      source.reverse match {
        case Nil => 0
        case x :: Nil => alphabet.indexOf(x) + 1
        case x :: xs => (alphabet.indexOf(x) + 1) * scala.math.pow(alphabet.length, index - 1).toInt +
           _combinationsCount(xs.reverse, alphabet, index - 1)
      }
    }

    source match {
      case Nil => 0
      case x :: Nil => alphabet.indexOf(x) + 1
      case x :: xs => _combinationsCount(source, alphabet, source.length)
    }
  }

  /**
    * Calculates state that should be received after shifting the init sequence state
    * of given alphabet "shift" number of times.
    * For example, for alphabet [1, 2, 3] and shift = 7 result equals [1, 2]
    **/
  def calculateShift[T](alphabet: List[T], shift: Int): List[T] = {

    import scala.math.pow

    def _calculateNumberOfElements(alphabet: List[T], shift: Int): Int = {
      if (alphabet.length >= shift) return 1
      Stream.from(1).take(Int.MaxValue).takeWhile(i => {
        (1 to i).toList.foldLeft(0)((acc, x) => acc + scala.math.pow(alphabet.length, x).toInt) < shift
      }).last + 1
    }

    if (shift == 1) return alphabet.take(1)

    val numberOfElements = _calculateNumberOfElements(alphabet, shift)
    if (numberOfElements == 1) return List(alphabet(shift - 1))

    // if numberOfElements = 3 then calculating sum of combinations of 1 and 2-digit numbers
    // (3 + 9 for alphabet [1, 2, 3])
    val sumPreviousCapacity = (1 to numberOfElements - 1).toList.foldLeft(0)(
      (acc, v) => acc + scala.math.pow(alphabet.length, v).toInt
    )
    val lastSymbolIndex = (1 to alphabet.length - 1).toList.
      takeWhile(i => i * pow(alphabet.length, numberOfElements - 1).toInt < shift - sumPreviousCapacity).length
    // remainder after setting the last symbol
    val remainder = shift - sumPreviousCapacity - lastSymbolIndex * pow(alphabet.length, numberOfElements - 1) +
      combinationsCount(List.fill(numberOfElements - 1)(alphabet(0)), alphabet) - 1
    val numberOfRemainedElements = _calculateNumberOfElements(alphabet, remainder.toInt)

    calculateShift(alphabet, remainder.toInt) ++
      List.fill(numberOfElements - 1 - numberOfRemainedElements)(alphabet(0)) ++ List(alphabet(lastSymbolIndex))
  }


  @throws(classOf[TimeoutException])
  private def _generateSequencesConcurrently[T](tasks: List[(Int, Int)], alphabet: List[T], threadCount: Int)
                                      (implicit timeout: Timeout):  ConcurrentHashMap[(Int, Int), List[List[T]]] = {
    println(s"---- Starting parallel generation of sequences ----")
    val system = ActorSystem("GenerateSequenceSystem")
    val threads = (1 to threadCount).map(i => system.actorOf(Worker.props, name = ("worker_" + i)))
    val managerActor = system.actorOf(ActorManager.props(tasks, alphabet, threads.toList), name = "managerActor")

    val future = managerActor ? Start
    try {
      val result = Await.result(future, timeout.duration).asInstanceOf[WorkDone[T]]
      println(s"---- Parallel generation completed ----")
      result.resultMap
    } catch {
      case e: TimeoutException => {
        throw new TimeoutException(s"Couldn't complete your task within given timeout of ${timeout}")
      }
    } finally {
      managerActor ! PoisonPill
      system.terminate
    }
  }

  def main(args: Array[String]) = {
    seqCombinations(List(1, 2, 3, 4), 5, 4, 10, Timeout(30 seconds)).foreach(println)
    //println(generateCombsOnInterval(List(1, 2), List(2, 1, 1), List(1, 2)))
    //println(combinationsCount(List(1, 2), List(1, 2, 3)))
    //println(calculateShift(List(1, 2, 3), 7))
  }
}
