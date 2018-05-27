package com.example.sortings

import scala.util.Random

object Sortings {

  def bubbleSort[T <% Ordered[T]](xs : List[T]) : List[T] = {
    def shiftMinToBeginning(xs : List[T]) : List[T] = {
      xs match {
        case x :: Nil => x :: Nil
        case xs => xs.min :: xs.patch(xs.indexOf(xs.min), Nil, 1)
      }
    }

    xs match {
      case Nil => Nil
      case x :: Nil => x :: Nil
      case x :: xs => {
        val sortedIteration = shiftMinToBeginning(x :: xs)
        val (left, right) = sortedIteration.splitAt(1)
        left ++ bubbleSort(right)
      }
    }
  }

  def mergeSort[T <% Ordered[T]](xs : List[T]) : List[T] = {

    def mergeLists(xs : List[T], ys : List[T]) : List[T] = {
      (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs, y :: ys) => {
          if (x > y) y :: mergeLists(x :: xs, ys)
          else x :: mergeLists(xs, y :: ys)
        }
      }
    }

    xs match {
      case Nil => Nil
      case x :: Nil => x :: Nil
      case xs => {
        val (left, right) = xs.splitAt(xs.length / 2)
        mergeLists(mergeSort(left), mergeSort(right))
      }
    }
  }

  def insertSort[T <% Ordered[T]](xs : List[T]) : List[T] = {
    def putToSorted(item : T, list : List[T]) : List[T] = {
      list match {
        case Nil => item :: Nil
        case x :: xs => {
          if (x >= item) item :: x :: xs
          else x :: putToSorted(item, xs)
        }
      }
    }

    xs match {
      case Nil => Nil
      case x :: Nil => x :: Nil
      case x :: xs => putToSorted(x, insertSort(xs))
    }
  }

  def quickSort[T <% Ordered[T]](list : List[T]) : List[T] = {
    list match {
      case Nil => Nil
      case x :: xs => quickSort(xs.filter(_ < x)) ++ (x :: Nil) ++ quickSort(xs.filter(_ > x))
    }
  }

  private def isListSorted[T <% Ordered[T]](list : List[T]) : Boolean = {
    list match {
      case Nil => true
      case x :: Nil => true
      case x :: y :: xs => if (x <= y) isListSorted(y :: xs) else false
    }
  }

  private def shuffleList[T](xs : List[T]) : List[T] = {
    xs match {
      case Nil => Nil
      case x :: Nil => x :: Nil
      case x :: xs => {
        val randomIndex = Random.nextInt(xs.length + 1)
        (x :: xs)(randomIndex) :: shuffleList((x :: xs).patch(randomIndex, Nil, 1))
      }
    }
  }

  def main(args: Array[String]): Unit = {

    val list = shuffleList((1 to 10 by 1).toList)
    println(s"source list: ${list}")
    println(s"bubble sort: ${Sortings.bubbleSort(list)} / is sorted: ${isListSorted(bubbleSort(list))}")
    println(s"merge sort: ${Sortings.mergeSort(list)} / is sorted: ${isListSorted(mergeSort(list))}")
    println(s"insertSort sort: ${Sortings.insertSort(list)} / is sorted: ${isListSorted(insertSort(list))}")
    println(s"quickSort sort: ${Sortings.quickSort(list)} / is sorted: ${isListSorted(quickSort(list))}")
  }
}
