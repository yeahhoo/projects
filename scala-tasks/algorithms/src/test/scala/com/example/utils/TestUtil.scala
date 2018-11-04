package com.example.utils

import scala.annotation.tailrec
import scala.util.Random

object TestUtil {

  def shuffleList[T](xs: List[T]): List[T] = {

    @tailrec
    def _shuffleList(xs: List[T], result: List[T]): List[T] = {
      xs match {
        case Nil => result
        case x :: Nil => x :: result
        case x :: xs => {
          val randomIndex = Random.nextInt(xs.length + 1)
          _shuffleList((x :: xs).patch(randomIndex, Nil, 1), (x :: xs) (randomIndex) :: result)
        }
      }
    }

    _shuffleList(xs, Nil)
  }
}