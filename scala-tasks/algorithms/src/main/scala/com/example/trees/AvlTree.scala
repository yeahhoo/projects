package com.example.trees

import scala.util.Random
import com.example.trees.avltree._

/** a little hack to avoid interfering with node classes of other tree types. */
package com.example.trees.avltree {
  sealed abstract class Node[+T]

  case object EmptyNode extends Node[Nothing]

  case class Tree[+T](
                       val value: T,
                       val height: Int,
                       val left: Node[T],
                       val right: Node[T]
                     ) extends Node[T] {

    def this(value: T) = this(value, 1, EmptyNode, EmptyNode)
  }
}

class AvlTree[T <% Ordered[T]] {

  var root : Node[T] = EmptyNode

  /** Prints tree in human-readable format. */
  def printTree(): Unit = {
    def _traverse(node: Node[T], h: Int, shift: Int, parentShift: Int): List[(Int, T, Int, Boolean)] = {
      val delta = scala.math.abs(shift - parentShift) / 2
      val ls = shift - delta
      val rs = shift + delta
      node match {
        case EmptyNode => Nil
        case Tree(_, _, ln@Tree(_, _, _, _), rn@Tree(_, _, _, _)) => {
          ((h, ln.value, ls, false) ::
            (h, rn.value, rs, true) :: (_traverse(ln, h - 1, ls, shift) ++ _traverse(rn, h - 1, rs, shift)))
        }
        case Tree(_, _, ln@Tree(_, _, _, _), EmptyNode) => (h, ln.value, ls, false) :: _traverse(ln, h - 1, ls, shift)
        case Tree(_, _, EmptyNode, rn@Tree(_, _, _, _)) => (h, rn.value, rs, true) :: _traverse(rn, h - 1, rs, shift)
        case Tree(_, _, EmptyNode, EmptyNode) => Nil
      }
    }

    def _calcMaxSpan(node: Node[T]): Int = {
      def _findLongest(node: Node[T]): Int = node match {
        case EmptyNode => 0
        case Tree(v, _, l, r) => math.max(v.toString.length, math.max(_findLongest(l), _findLongest(r)))
      }
      math.pow(2, height(node) - 1).toInt * (_findLongest(node) + 2)
    }

    val res = root match {
      case EmptyNode => {
        println("tree is empty")
        return ()
      }
      case Tree(value, _, _, _) => {
        val shift: Int = _calcMaxSpan(root) / 2
        ((height(root), value, shift, true) :: _traverse(root, height(root) - 1, shift, 0))
          .sortWith((x, y) => {
            if (x._1 == y._1) x._3 < y._3
            else x._1 > y._1
          })
      }
    }

    val grouped = res.groupBy(_._1)
    (height(root) to 1 by -1).toList.foreach(i => {
      val numberString = grouped(i).map({ case (l, v, s, _) => ((List.fill(s)(" ") :+ v.toString)).mkString("") })
        .reduceRight((x, y) => {
          val end = y.slice(x.length, y.length)
          //if (end(0) != " ") x + " " + end else x + end
          x + end
        })
      val arrowString = grouped(i).map({ case (_, _, s, isRight) => {
        val arrow = if (isRight) "\\" (0) else "/"
        ((List.fill(s)(" ") :+ arrow)).mkString("")
      }
      }).reduceRight((x, y) => {
        x + y.slice(x.length, y.length)
      })
      println(arrowString)
      println(numberString)
    })
  }

  /** Returns the height of the tree */
  def height[T](node : Node[T]) : Int = {
    node match {
      case EmptyNode => 0
      case Tree(_, height, _, _) => height
    }
  }

  /** Checks whether given value exists in the tree. */
  def contains(value : T): Boolean = {

    def contains(value : T, node : Node[T]) : Boolean = {
      node match {
        case EmptyNode => false
        case current@Tree(v, _, left, right) => {
          if (value < v) contains(value, left)
          else if (value > v) contains(value, right)
          else true
        }
      }
    }

    root match {
      case EmptyNode => false
      case Tree(_, _, _, _) => contains(value, root)
    }
  }

  /** Inserts given value into the tree. */
  def insert(value : T): Node[T] = {

    def insert(value : T, node : Node[T]) : Tree[T] = {
      val tree : Tree[T] = node match {
        case EmptyNode => Tree[T](value, 1, EmptyNode, EmptyNode)
        case current@Tree(nodeValue, _, left, right) => {
          if (nodeValue < value) {
            val rightNode = insert(value, right)
            val newHeight = 1 + math.max(height(left), height(rightNode))
            Tree(nodeValue, newHeight, left, rightNode)
          } else if (nodeValue > value) {
            val leftNode = insert(value, left)
            val newHeight = 1 + math.max(height(leftNode), height(right))
            Tree(nodeValue, newHeight, leftNode, right)
          }
          else current
        }
      }
      rebalance(tree)
    }

    root = root match {
      case EmptyNode => new Tree(value)
      case Tree(_, _, _, _) => insert(value, root)
    }
    root
  }

  /** Removes node from the tree by given value. */
  def delete(value : T) : Boolean = {

    def delete(node : Node[T], value : T) : (Node[T], Boolean) = {
      val removedNode : (Node[T], Boolean) = node match {
        case EmptyNode => (EmptyNode, false)
        case t@Tree(currentValue, h, EmptyNode, EmptyNode) => {
          if (currentValue == value) (EmptyNode, true)
          else (t, false)
        }
        case Tree(currentValue, h, left, EmptyNode) => {
          if (currentValue > value) {
            val (newNode, isRemoved) = delete(left, value)
            (Tree(currentValue, 1, newNode, EmptyNode), isRemoved)
          }
          else if (currentValue == value) (left, true)
          else (EmptyNode, false)
        }
        case Tree(currentValue, h, EmptyNode, right) => {
          if (currentValue < value) {
            val (newNode, isRemoved) = delete(right, value)
            (Tree(currentValue, 1, EmptyNode, newNode), isRemoved)
          }
          else if (currentValue == value) (right, true)
          else (EmptyNode, false)
        }
        case Tree(currentValue, h, left@Tree(_, _, _, _), right@Tree(_, _, _, _)) => {
          if (currentValue > value) {
            val (nodeRemovedLeft, isRemoved) = delete(left, value)
            (Tree(currentValue, 1 + math.max(height(nodeRemovedLeft), height(right)), nodeRemovedLeft, right), isRemoved)
          }
          else if (currentValue < value) {
            val (nodeRemovedRight, isRemoved) = delete(right, value)
            (Tree(currentValue, 1 + math.max(height(left), height(nodeRemovedRight)), left, nodeRemovedRight), isRemoved)
          } else {
            def min(tree : Tree[T]) : Tree[T] = {
              tree match {
                case Tree(_, _, EmptyNode, _) => tree
                case Tree(_, _, left@Tree(_, _, _, _), _) => min(left)
              }
            }
            val nodeToRemove = min(right)
            val (newRight, isRemoved) = delete(right, nodeToRemove.value)
            (Tree(nodeToRemove.value, 1 + math.max(height(left), height(newRight)), left, newRight), isRemoved)
          }
        }
      }

      removedNode._1 match {
        case EmptyNode => (EmptyNode, removedNode._2)
        case t@Tree(_, _, _, _) => (rebalance(t), removedNode._2)
      }
    }

    root match {
      case EmptyNode => false
      case Tree(_,_ ,_,_) => {
        val (newRoot, res) = delete(root, value)
        root = newRoot
        res
      }
    }
  }

  private def rebalance(tree : Tree[T]) : Tree[T] = {
    val nodeBalance = balance(tree)
    if (nodeBalance > 1) {
      val rightBalance = balance(tree.right)
      if (rightBalance > 0) {
        leftRotate(tree)
      } else if (rightBalance < 0) {
        rightLeftRotate(tree)
      } else tree
    } else if (nodeBalance < -1) {
      val leftBalance = balance(tree.left)
      if (leftBalance < 0) {
        rightRotate(tree)
      } else if (leftBalance > 0) {
        leftRightRotate(tree)
      } else tree
    } else tree
  }

  private def leftRightRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, h, left@Tree(leftValue, _, leftSubTree, rightSubTree), right) = node
    val rotatedLeft = Tree(value, h, leftRotate(left), right)
    val rotatedRight = rightRotate(rotatedLeft)
    rotatedRight
  }

  private def rightLeftRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, h, left, right@Tree(leftValue, _, leftSubTree, rightSubTree)) = node
    val rotatedRight = Tree(value, h, left, rightRotate(right))
    val rotatedLeft = leftRotate(rotatedRight)
    rotatedLeft
  }

  private def rightRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, _, Tree(leftValue, _, leftSubTree, rightSubTree), right) = node
    val rightHeight = 1 + math.max(height(rightSubTree), height(right))
    val newRight = Tree(value, rightHeight, rightSubTree, right)
    val newParentHeight = 1 + math.max(height(leftSubTree), height(newRight))
    val tree = Tree(leftValue, newParentHeight, leftSubTree, newRight)
    tree
  }

  private def leftRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, _, left, Tree(rightValue, _, leftSubTree, rightSubTree)) = node
    val leftHeight = 1 + math.max(height(leftSubTree), height(left))
    val newLeft = Tree(value, leftHeight, left, leftSubTree)
    val newParentHeight = 1 + math.max(height(rightSubTree), height(newLeft))
    val tree = Tree(rightValue, newParentHeight, newLeft, rightSubTree)
    tree
  }

  private def balance[T](node : Node[T]) : Int = {
    node match {
      case EmptyNode => 0
      case Tree(_, _, left, right) => height(right) - height(left)
    }
  }
}

object AvlTree {

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

  def main(args : Array[String]): Unit = {
    val tree = new AvlTree[Int]()

    val set = shuffleList((1 to 40 by 1).toList)
    val removeSet = shuffleList(set)

    println(s"source array: ${set}")
    println(s"remove array: ${removeSet}")

    set.foreach(value => tree.insert(value))

    tree.printTree()
    set.foreach(value => {
      if (!tree.contains(value)) {
        throw new RuntimeException(s"Couldn't find node with value: ${value}")
      }
    })

    removeSet.foreach(value => {
      println(s"------------------")
      println(s"removing: ${value}")
      tree.printTree()
      if (!tree.delete(value)) {
        throw new RuntimeException(s"Couldn't remove node with value: ${value}")
      }
    })
  }
}
