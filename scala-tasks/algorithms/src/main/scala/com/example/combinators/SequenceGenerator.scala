package com.example.combinators

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters

/** Generates sequences from given alphabet. Can be think of as "brute force" algorithm. */
object SequenceGenerator {

  /**
    * Generates sequences up to the specified length from given alphabet.
    * "chunksNumber" defines numbers of chunks to split source works into.
    * // todo make this algorithm parallel with actors.
    * For example, if alphabet equals [1, 2] and size = 2 then the output equals:
    * List(1),
    * List(2),
    * List(1, 1),
    * List(2, 1),
    * List(1, 2),
    * List(2, 2)
    *
    * */
  def seqCombinations[T](alphabet: List[T], size: Int, chunksNumber: Int): List[List[T]] = {

    // todo optimize it to tail recursion
    def _borderCombs(start: List[T], end: List[T], threshold: T): List[List[T]] = {
      if (start == end) Nil
      else if (start.forall(_ == threshold)) {
        start :: _borderCombs(List.fill(start.length + 1)(alphabet(0)), end, threshold)
      }
      else start :: _borderCombs(increment(start, threshold), end, threshold)
    }

    def increment(list: List[T], threshold: T): List[T] = {
      list match {
        case Nil => Nil
        case x :: xs => {
          if (x == threshold) alphabet(0) :: increment(xs, threshold) else alphabet(alphabet.indexOf(x) + 1) :: xs
        }
      }
    }

    def _seqCombinations(state: List[T], threshold: T): List[List[T]] = {
      if (state.forall(x => x == threshold)) Nil
      else state :: _seqCombinations(increment(state, alphabet.last), alphabet.last)
    }

    val init = List(alphabet(0))
    val end = List.fill(size)(alphabet.last)
    val combsCount = combinationsCount(end, alphabet)
    val partsNumber = (combsCount / chunksNumber.toDouble).toInt
    if (partsNumber == 0) {
      println("given chunksNumber is too high for the collection. Going to use default strategy")
      return _borderCombs(init, List.fill(size)(alphabet.last), alphabet.last) :+ List.fill(size)(alphabet.last)
    }

    val partsList = ((1 to combsCount by partsNumber).takeWhile(_ <= combsCount) :+ combsCount).distinct.toList
    // val calcList = (0 to partsList.length - 2).map(i => (calculateShift(alphabet, partsList(i)), calculateShift(alphabet, partsList(i + 1)))).toList
    // return calcList.map(x => _borderCombs(x._1, x._2, alphabet.last)).flatten :+ end
    val resultMap = new ConcurrentHashMap[(Int, Int), List[List[T]]]()
    (0 to partsList.length - 2).foreach(i => resultMap.put((partsList(i), partsList(i + 1)), Nil))
    resultMap.entrySet().stream().forEach(t => {
      resultMap.put((t.getKey()._1,
        t.getKey()._2),
        _borderCombs(calculateShift(alphabet, t.getKey()._1), calculateShift(alphabet, t.getKey()._2), alphabet.last))
    })

    //val sortedKeys = resultMap.keySet().toList.sortBy(t => t._1)
    val sortedKeys = JavaConverters.asScalaIterator(resultMap.keySet().iterator()).toList.sortBy(t => t._1)
    sortedKeys.map(t => resultMap.get(t)).flatten :+ end
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

  def main(args : Array[String]) = {
    seqCombinations(List(1, 2, 3, 4), 4, 10).foreach(println)
    //println(combinationsCount(List(1, 2), List(1, 2, 3)))
    //println(calculateShift(List(1, 2, 3), 7))
  }
}
