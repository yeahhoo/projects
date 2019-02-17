package com.example.trees

import scala.util.Random
import com.example.trees.btree._

/** a little hack to avoid interfering with node classes of other tree types. */
package com.example.trees.btree {
  sealed abstract class Node[+T]

  case object EmptyNode extends Node[Nothing]

  case class Tree[+T](
                       val keys: List[T],
                       val children: List[Tree[T]],
                       val height: Int
                     ) extends Node[T] {


    def this(keys: List[T]) = this(keys, List.empty, 1)
  }
}

/**
  * BTree implementation. Inspired by:
  * https://www.cs.usfca.edu/~galles/visualization/BTree.html
  * https://en.wikipedia.org/wiki/B-tree
  **/
class BTree[T <% Ordered[T]](degree: Int) {
  var root : Node[T] = EmptyNode

  /** Prints tree in human-readable format. */
  def printTree(): Unit = {

    // returns 4-dimensional list that represents given btree.
    // 1st level: list of rows (height)
    // 2nd level: list of blocks in rows
    // 3rd level: nodes in the block
    // 4th level: represents items of node
    def _traverse(trees: List[List[Tree[T]]]): List[List[List[List[T]]]] = {
      trees match {
        case Nil => Nil
        case _ => {
          val values = trees.map(treeBlock => treeBlock.map(t => t.keys))
          val children = trees.map(treeBlock => treeBlock.map(t => t.children)).flatten
          if (values.forall(l => l.isEmpty)) Nil :: _traverse(children) else values :: _traverse(children)
        }
      }
    }

    def mapToStrings(foldedTree: List[List[List[List[T]]]], prev: String = ""): List[String] = {

      def mapBlock(block: List[List[T]], freeSpace: Int): String = {
        val nodeLength = block.map(b => b.toString().length).max
        val nodes = block.map(b => {
          if (b.toString().length == nodeLength) b.toString()
          else {
            val shift = List.fill((nodeLength - b.toString().length) / 2)(" ").mkString
            shift + b.toString() + shift
          }
        })
        val spaceToFill = freeSpace - nodes.map(_.length).reduce((_ + _))
        val blockShiftLength = spaceToFill / (block.size + 1)
        val shift = List.fill(blockShiftLength)(" ").mkString
        shift + nodes.mkString(shift) + shift
      }

      def findBorders(row: List[List[List[T]]], prev: String = ""): List[Int] = {

        def splitByOccurTimes(str: String, times: Int): (String, String) = {
          val part = str.split("\\|", times + 1).toList.take(times).mkString("|")
          val rest = if (str.length > part.length + 1) str.substring(part.length + 1) else ""
          (part, rest)
        }

        row match {
          case Nil => List(0)
          case x :: Nil => List(prev.length)
          case x :: xs => {
            val (part, rest) = splitByOccurTimes(prev, x.length)
            part.length :: findBorders(xs, rest)
          }
        }
      }

      foldedTree match {
        case Nil => List("")
        case row :: xs => {
          row match {
            case Nil => mapToStrings(xs)
            case block :: Nil => {
              // making second line after root root node
              val borders = findBorders(block.map(List(_)), prev)
              val strBlocks = block.map(List(_)).zip(borders).map({
                case (block, blockLength) => mapBlock(block, blockLength)})
              val res = s"${strBlocks.mkString("|")}"
              res :: mapToStrings(xs, res)
            }
            case block :: blocks => {
              if (prev.isEmpty) {
                // making first line
                val blockList = row.map(b => b.mkString(" "))
                val res = s"${blockList.mkString("   |   ")}"
                res :: mapToStrings(xs, res)
              } else {
                val borders = findBorders(row, prev)
                val strBlocks = row.zip(borders).map({case (block, blockLength) => mapBlock(block, blockLength)})
                val res = s"${strBlocks.mkString("|")}"
                res :: mapToStrings(xs, res)
              }
            }
          }
        }
      }
    }

    root match {
      case EmptyNode => println(s"---- Tree is empty -----")
      case t@Tree(_, _, _) => mapToStrings(_traverse(List(List(t))).reverse).reverse.foreach(println)
    }
  }

  /** Checks whether given value is presented in the tree. */
  def contains(value: T): Boolean = {

    def contains(value: T, node: Tree[T]): Boolean = {
      node.children match {
        case Nil => findIndexInBlockToInsert(value, node.keys) match {
          case None => true
          case Some(_) => false
        }
        case children => findIndexInBlockToInsert(value, node.keys) match {
          case None => true
          case Some(index) => contains(value, children(index))
        }
      }
    }

    root match {
      case EmptyNode => false
      case t@Tree(_, _, _) => contains(value, t)
    }
  }

  /** Inserts given value into the tree. */
  def insert(value: T): Boolean = {

    def insert(value: T, node: Tree[T]): (Tree[T], Boolean) = {
      node.children match {
        case Nil => {
          val leafNode = putNewKeyIntoBlock(value, node)
          if (isNodeFull(leafNode)) (splitTree(leafNode), true) else (leafNode, true)
        }
        case _ => {
          findIndexInBlockToInsert(value, node.keys) match {
            case None => (node, false) // duplicates are not allowed
            case Some(index) => {
              val (newTree, isAdded) = insert(value, node.children(index))
              if (!isAdded) (node, false) else {
                if (node.height == newTree.height) {
                  // sub node was broken up
                  val merged = new Tree(node.keys.patch(index, newTree.keys, 0),
                    node.children.patch(index, newTree.children, 1),
                    node.height)
                  if (isNodeFull(merged)) (splitTree(merged), true) else (merged, true)
                } else {
                  // means item was just added to the block
                  (new Tree(node.keys, node.children.patch(index, List(newTree), 1), node.height), true)
                }
              }
            }
          }
        }
      }
    }

    def putNewKeyIntoBlock(value: T, tree: Tree[T]): Tree[T] = {
      findIndexInBlockToInsert(value, tree.keys) match {
        case None => tree
        case Some(index) => new Tree(tree.keys.patch(index, List(value), 0), tree.children, tree.height)
      }
    }

    def splitTree(tree: Tree[T]): Tree[T] = {
      val headIndex = Math.ceil(degree / 2d).toInt - 1
      val leftTree = new Tree(tree.keys.take(headIndex), tree.children.take(headIndex + 1), tree.height)
      val rightTree = new Tree(tree.keys.drop(headIndex + 1), tree.children.drop(headIndex + 1), tree.height)
      new Tree(List(tree.keys(headIndex)), List(leftTree, rightTree), tree.height + 1)
    }

    val (newRoot, isInserted) = root match {
      case EmptyNode => (new Tree(List(value)), true)
      case t@Tree(_, _, _) => {
        val res = insert(value, t)
        (res._1, res._2)
      }
    }
    root = newRoot
    isInserted
  }

  /**
    * Finds index in block for inserting new value using a small modification of binary search algorithm.
    * Value is not being added if it is already present in the block.
    * */
  private def findIndexInBlockToInsert(value: T, list: List[T]): Option[Int] = {

    def _find(value: T, list: List[T], low: Int, high: Int): Option[Int] = {
      // if value is already contained in the list then mark it as non-appropriate since duplicates are prohibited
      if (list(low) == value || list(high) == value) return None
      if (low >= high) {
        if (list(low) > value) return Some(low) else return Some(low + 1)
      }

      val middle = (low + high) / 2
      val size = list.length
      if (list(middle) == value) None
      else if (list(middle) < value) _find(value, list, if (middle + 1 >= size) size - 1 else middle + 1, high)
      else _find(value, list, low, if (middle - 1 < 0) 0 else middle - 1)
    }

    _find(value, list, 0, list.length - 1)
  }

  /** Removes node from the tree by given value. */
  def delete(value: T): Boolean = {

    def delete(value: T, node: Tree[T]): (Tree[T], Boolean) = {
      findIndexInBlockToInsert(value, node.keys) match {
        case None => {
          // we need to find index because value was found in given block
          val index = findIndexInBlock(value, node.keys)
          node.children match {
            case Nil => {
              // no children, means that we need to remove the value from given node
              ((new Tree(node.keys.patch(index.get, List(), 1), node.children, node.height)), true)
            }
            case xs => {
              def max(tree: Tree[T]): T = tree.children match {
                case Nil => tree.keys.last
                case xs => max(xs.last)
              }

              val maxValue = max(node.children(index.get))
              val (removedNode, isRemoved) = delete(maxValue, node.children(index.get))
              val newParent = new Tree(node.keys.patch(index.get, List(maxValue), 1), node.children, node.height)
              (rebalance(newParent, removedNode, index.get), isRemoved)
            }
          }
        }
        case Some(index) => {
          if (node.children.isEmpty) (node, false)
          else {
            val subTree = node.children(index)
            val (newNode, isRemoved) = delete(value, subTree)
            (rebalance(node, newNode, index), isRemoved)
          }
        }
      }
    }

    def rebalance(parent: Tree[T], child: Tree[T], index: Int): Tree[T] = {
      if (child.keys.length < minKeysNumber) {
        // we can pinch off one key from neighbor or parent and give it to child in need
        if (index == 0) {
          val rightNeighbor = parent.children(index + 1)
          if (rightNeighbor.keys.length + child.keys.length + 1 >= degree) {
            // pinch off one key from right neighbor
            val newNode = new Tree(child.keys ++ List(parent.keys.head),
              child.children ++ rightNeighbor.children.take(1), child.height)
            val newNeighbor = new Tree(rightNeighbor.keys.drop(1), rightNeighbor.children.drop(1), rightNeighbor.height)
            new Tree(parent.keys.patch(index, List(rightNeighbor.keys.head), 1),
              newNode :: newNeighbor :: parent.children.drop(2), parent.height)
          } else {
            // merge node with neighbor
            val newNode = new Tree(child.keys ++ List(parent.keys.head) ++ rightNeighbor.keys,
              child.children ++ rightNeighbor.children, child.height)
            new Tree(parent.keys.drop(1), newNode :: parent.children.drop(2), parent.height)
          }
        } else {
          val leftNeighbor = parent.children(index - 1)
          val ph = parent.height
          if (leftNeighbor.keys.length + child.keys.length + 1 >= degree) {
            // pinch off one key from left neighbor
            val newNode = new Tree(parent.keys(index - 1) :: child.keys, leftNeighbor.children.takeRight(1) ++ child.children, ph - 1)
            val newNeighbor = new Tree(leftNeighbor.keys.dropRight(1), leftNeighbor.children.dropRight(1), ph - 1)
            new Tree(parent.keys.patch(index - 1, List(leftNeighbor.keys.last), 1),
              parent.children.take(index - 1) ++ List(newNeighbor, newNode) ++ parent.children.drop(index + 1), ph)
          } else {
            // merge node with neighbor
            val newNode = new Tree(leftNeighbor.keys ++ List(parent.keys(index - 1)) ++ child.keys,
              leftNeighbor.children ++ child.children, child.height)
            new Tree(parent.keys.patch(index - 1, List(), 1),
              parent.children.take(index - 1) ++ List(newNode) ++ parent.children.drop(index + 1), ph)
          }
        }
      }
      else if (child.keys.length >= minKeysNumber) {
        // we need to simply update parent node with new child
        new Tree(parent.keys, parent.children.patch(index, List(child), 1), parent.height)
      } else {
        // node is already balanced so no action required
        parent
      }
    }

    root match {
      case EmptyNode => false
      case t@Tree(_, _, _) => {
        val (newRoot, isRemoved) = delete(value, t)
        if (newRoot.keys.isEmpty && !newRoot.children.isEmpty && isRemoved) {
          root = newRoot.children.head
        } else if (newRoot.keys.isEmpty && newRoot.children.isEmpty) {
          root = EmptyNode
        } else {
          root = newRoot
        }
        isRemoved
      }
    }
  }

  /** Finds index of given value in the list using binary search algorithm. */
  private def findIndexInBlock(value: T, list: List[T]): Option[Int] = {

    def _find(value: T, list: List[T], low: Int, high: Int): Option[Int] = {
      if (low > high) return None

      val middle = (low + high) / 2
      if (list(middle) == value) Some(middle)
      else if (list(middle) > value) _find(value, list, low, middle - 1)
      else _find(value, list, middle + 1, high)
    }

    _find(value, list, 0, list.length - 1)
  }

  private def minKeysNumber: Int = Math.ceil(degree / 2.0).toInt - 1

  private def isNodeFull(tree: Tree[T]): Boolean = {
    tree.keys.length >= degree
  }
}

object BTree {

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

  def main(args: Array[String]): Unit = {
    println(s"----- BTree -------")
    val btree = new BTree[Int](3)

    val set = shuffleList((1 to 40 by 1).toList)
    val removeSet = shuffleList(set)

    println(s"source array: ${set}")
    println(s"remove array: ${removeSet}")

    set.foreach(value => btree.insert(value))

    set.foreach(value => {
      if (!btree.contains(value)) {
        throw new RuntimeException(s"Couldn't find node with value: ${value}")
      }
    })

    removeSet.foreach(value => {
      println(s"------------------")
      btree.printTree()
      println(s"\nremoving: ${value}")
      if (!btree.delete(value)) {
        throw new RuntimeException(s"Couldn't remove node with value: ${value}")
      }
    })
  }
}