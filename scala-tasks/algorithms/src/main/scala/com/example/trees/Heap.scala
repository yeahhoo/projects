package com.example.trees

import scala.util.Random

/** Max Heap implementation. */
class Heap[T <% Ordered[T]] private (val data: Seq[T]) {

  /** Inserts given value in the heap. */
  def insert(value: T): Heap[T] = {

    def _insert(value: T, data: Seq[T], idx: Int): Heap[T] = idx match {
      case idx if idx <= 0 => new Heap(data)
      case _ => {
        if (data(getParentIndex(idx)) >= data(idx)) new Heap(data)
        else _insert(value, swap(data, idx, getParentIndex(idx)), getParentIndex(idx))
      }
    }

    _insert(value, data :+ value, data.length)
  }

  /** Removes item out of the heap. */
  def delete(): Option[(T, Heap[T])] = {

    def _delete(data: Seq[T], idx: Int): Heap[T] = idx match {
      case idx if getLeftChildIndex(idx) >= data.length => new Heap(data)
      case _ => {
        val isRightChildExist = getLeftChildIndex(idx) + 1 <= data.length - 1
        if (isRightChildExist && data(getLeftChildIndex(idx) + 1) > data(getLeftChildIndex(idx))
          && data(getLeftChildIndex(idx) + 1) > data(idx)) {
          _delete(swap(data, getLeftChildIndex(idx) + 1, idx), getLeftChildIndex(idx) + 1)
        }
        else if (data(getLeftChildIndex(idx)) > data(idx)) {
          _delete(swap(data, getLeftChildIndex(idx), idx), getLeftChildIndex(idx))
        } else new Heap(data)
      }
    }

    if (data.isEmpty) None else Some((data.head, _delete(swap(data, 0, data.length - 1).dropRight(1), 0)))
  }

  /** Peeks head value. */
  def peek(): Option[T] = data.headOption

  /** Returns size of the heap. */
  def size(): Int = data.length

  /** Prints heap. */
  def print(): Unit = println(s"Data stored in heap: ${data}")

  /** Unfolds heap to list. */
  def unfold(): List[T] = {
    (1 to data.length).foldLeft((List.empty[T], new Heap(data)))((p, _) => {
      val (list, heap) = p
      heap.delete() match {
        case None => (list, heap)
        case Some((item, newHeap)) => (list :+ item, newHeap)
      }
    })._1
  }

  private def getParentIndex(idx: Int): Int = if (idx % 2 == 0) (idx / 2) - 1 else idx / 2

  private def getLeftChildIndex(idx: Int): Int = idx * 2 + 1

  private def swap(data: Seq[T], idx1: Int, idx2: Int): Seq[T] = {
    val tmpValue = data(idx1)
    val tmpSeq = data.patch(idx1, List(data(idx2)), 1)
    tmpSeq.patch(idx2, List(tmpValue), 1)
  }
}

object Heap {

  def shuffleList[T](xs : List[T]) : List[T] = {
    xs match {
      case Nil => Nil
      case x :: Nil => x :: Nil
      case x :: xs => {
        val randomIndex = Random.nextInt(xs.length + 1)
        (x :: xs)(randomIndex) :: shuffleList((x :: xs).patch(randomIndex, Nil, 1))
      }
    }
  }

  def apply[T <% Ordered[T]](xs: T*): Heap[T] = xs.foldLeft(new Heap[T](Seq.empty))((h, i) => h.insert(i))

  def main(args: Array[String]): Unit = {
    val heap = Heap[Int](shuffleList((1 to 20 by 1).toList):_*)
    heap.print()

    val foldedList = heap.unfold()
    println(s"folded list: ${foldedList}")
  }
}
