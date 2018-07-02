package com.example.combinators

/**
  * Contains util combinations/permutations methods.
  * Inspired by: http://vkostyukov.net/posts/combinatorial-algorithms-in-scala/
  * */
object Combinator {

  /**
    * Generates all possible variations with given size out of elements of given list.
    * For example, result of the function for the List(1, 2, 3) and size = 2 equals to:
    * List(1, 2)
    * List(1, 3)
    * List(2, 1)
    * List(2, 3)
    * List(3, 1)
    * List(3, 2)
    **/
  def variations[T](list: List[T], groupSize: Int): List[List[T]] = {
    list match {
      case Nil => List(Nil)
      case x :: Nil => List(List(x))
      case _ => {
        if (groupSize == 1) list.map(List(_))
        else {
          for {
            num <- (0 to list.length - 1).toList
            res <- variations(list.take(num) ++ list.drop(num + 1), groupSize - 1).map(list(num) :: _)
          } yield res
        }
      }
    }
  }

  /**
    * Generates all possible permutations of given list. For example, if list equals List(1, 2, 3) then the result is:
    * List(1, 2, 3)
    * List(1, 3, 2)
    * List(2, 1, 3)
    * List(2, 3, 1)
    * List(3, 1, 2)
    * List(3, 2, 1)
    **/
  def permutations[T](list: List[T]): List[List[T]] = {
    variations(list, list.length)
  }

  /**
    * Generates all possible subsets consisting of elements from given list with size not exceeding groupSize.
    * Subset differs from the combination in that it isn't ordered - elements from the end of the given list would
    * appear in the beginning of a subset.
    * For example, result of the function for List(1, 2, 3) and groupSize = 3 equals to:
    * List(1)
    * List(1, 2)
    * List(1, 2, 3)
    * List(1, 3)
    * List(1, 3, 2)
    * List(2)
    * List(2, 1)
    * List(2, 1, 3)
    * List(2, 3)
    * List(2, 3, 1)
    * List(3)
    * List(3, 1)
    * List(3, 1, 2)
    * List(3, 2)
    * List(3, 2, 1)
    * */
  def subsets[T](list: List[T], groupSize: Int): List[List[T]] = {
    // the most outer list stores combinations collected for one full pass.
    // elements in the middle list start with the same item.
    def _subsets[T](list: List[T], groupSize: Int): List[List[List[T]]] = {
      list match {
        case Nil => Nil
        case x :: Nil => List(List(List(x)))
        case _ => {
          if (groupSize == 1) list.map(x => List(List(x)))
          else {
            for {
              num <- (0 to list.length - 1).toList
              res <- _subsets(list.take(num) ++ list.drop(num + 1), groupSize - 1)
            } yield List(List(list(num))) ++ res.map(list(num) :: _)
          }
        }
      }
    }

    //(1 to list.length).toList.map(variations(list, _)).flatten
    _subsets(list, groupSize).flatten.distinct
  }

  /**
    * Generates all possible subsets with repeating elements. Max size of any combination doesn't exceed list size.
    * For example, result of the function for List(1, 2) equals to:
    * List(1)
    * List(2)
    * List(1, 1)
    * List(1, 2)
    * List(2, 1)
    * List(2, 2)
    * */
  def subsetsWithRepeats[T](list : List[T]) : List[List[T]] = {
    val repeats = list.map(value => List.fill(list.length)(value)).flatten
    //(1 to list.length).toList.map(variations(repeats, _)).flatten.distinct
    (1 to list.length).toList.map(subsets(repeats, _)).flatten.distinct
  }

  /**
    * Generates all possible ordered combinations with given size out of given alphabet.
    * For example, result of the function for List(0, 1) and combinationSize = 3 equals to:
    * List(0, 0, 0)
    * List(1, 0, 0)
    * List(0, 1, 0)
    * List(1, 1, 0)
    * List(0, 0, 1)
    * List(1, 0, 1)
    * List(0, 1, 1)
    * List(1, 1, 1)
    **/
  def comboSets[T](alphabet: List[T], combinationSize: Int): List[List[T]] = {
    if (combinationSize > alphabet.length) {
      subsetsWithRepeats(List.fill(combinationSize - alphabet.length)(alphabet.head) ++ alphabet)
        .filter(_.length == combinationSize)
    } else {
      subsetsWithRepeats(alphabet).filter(_.length == combinationSize)
    }
  }

  /**
    * Generates all possible combinations of given size.
    * Analog of the default [[scala.collection.immutable.List.combinations[A]]] method.
    * Combination differs from subset in that it's ordered - order of elements wouldn't be shuffled in combinations.
    * For example, result of the function for List(1, 2, 3) and size = 2 equals to:
    * List(1, 2)
    * List(1, 3)
    * List(2, 3)
    * */
  def combinations[T](list: List[T], size: Int): List[List[T]] = {
    def _combinations[T](list: List[T], size: Int): List[List[T]] = {
      list match {
        case Nil => Nil
        case x :: xs => {
          if (size == 1) (x :: xs).map(List(_))
          else _combinations(xs, size - 1).map(x :: _) ++ _combinations(xs, size)
        }
      }
    }

    if (size > list.length) _combinations(list, list.length)
    else _combinations(list, size)
  }

  /**
    * Generates combinations out of the elements of given list with size not exceeding size of the list.
    * For example, result of the function for List(1, 2, 3) equals to:
    * List(1)
    * List(2)
    * List(3)
    * List(2, 3)
    * List(1, 2)
    * List(1, 3)
    * List(1, 2, 3)
    * */
  def combinations[T](list: List[T]): List[List[T]] = {
    list match {
      case Nil => Nil
      case x :: xs => List(List(x)) ++ combinations(xs) ++ combinations(xs).map(x :: _)
    }
  }

  def main(args : Array[String]) = {
    //println(s"${combinations(List(1, 2, 3))}")
    //println(s"${combinations(List(1, 2, 3), 2)}")
    //println(s"${variations(List(1, 2, 3), 2)}")
    //println(s"${permutations(List(1, 2, 3))}")
    //println(s"${subsets(List(1, 2, 3), 3)}")
    println(s"${subsets(List(1, 2, 3, 4), 2)}")
    //println(s"${subsetsWithRepeats("abc".toList)}")
    //println(s"${comboSets(List(0, 1, 2), 3)}")
    //println(s"${listWithThresholdCombos(3, 2)}")
  }
}
