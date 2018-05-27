package com.example.trees.avl

import scala.util.Random

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

class AvlTree[T <% Ordered[T]] {

  var root : Node[T] = EmptyNode

  def height[T](node : Node[T]) : Int = {
    node match {
      case EmptyNode => 0
      case Tree(_, height, _, _) => height
    }
  }

  def balance[T](node : Node[T]) : Int = {
    node match {
      case EmptyNode => 0
      case Tree(_, _, left, right) => height(right) - height(left)
    }
  }

  def get(value : T) : Node[T] = {

    def get(value : T, node : Node[T]) : Node[T] = {
      node match {
        case EmptyNode => EmptyNode
        case current@Tree(v, _, left, right) => {
          if (value < v) get(value, left)
          else if (value > v) get(value, right)
          else current
        }
      }
    }

    root match {
      case EmptyNode => EmptyNode
      case Tree(_, _, _, _) => get(value, root)
    }
  }

  def insert(value : T) : Node[T] = {

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

  def rebalance(tree : Tree[T]) : Tree[T] = {
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

  def leftRightRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, h, left@Tree(leftValue, _, leftSubTree, rightSubTree), right) = node
    val rotatedLeft = Tree(value, h, leftRotate(left), right)
    val rotatedRight = rightRotate(rotatedLeft)
    rotatedRight
  }

  def rightLeftRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, h, left, right@Tree(leftValue, _, leftSubTree, rightSubTree)) = node
    val rotatedRight = Tree(value, h, left, rightRotate(right))
    val rotatedLeft = leftRotate(rotatedRight)
    rotatedLeft
  }

  def rightRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, _, Tree(leftValue, _, leftSubTree, rightSubTree), right) = node
    val rightHeight = 1 + math.max(height(rightSubTree), height(right))
    val newRight = Tree(value, rightHeight, rightSubTree, right)
    val newParentHeight = 1 + math.max(height(leftSubTree), height(newRight))
    val tree = Tree(leftValue, newParentHeight, leftSubTree, newRight)
    tree
  }

  def leftRotate(node : Tree[T]) : Tree[T] = {
    val newNode@Tree(value, _, left, Tree(rightValue, _, leftSubTree, rightSubTree)) = node
    val leftHeight = 1 + math.max(height(leftSubTree), height(left))
    val newLeft = Tree(value, leftHeight, left, leftSubTree)
    val newParentHeight = 1 + math.max(height(rightSubTree), height(newLeft))
    val tree = Tree(rightValue, newParentHeight, newLeft, rightSubTree)
    tree
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

    val set = shuffleList((1 to 1000 by 1).toList)
    println(s"source array: ${set}")

    set.foreach(value => {
      tree.insert(value)
    })

    set.foreach(value => {
      if (tree.get(value) == EmptyNode) {
        throw new RuntimeException(s"Couldn't find node with value: ${value}")
      }
    })

    shuffleList(set).foreach(value => {
      if (!tree.delete(value)) {
        throw new RuntimeException(s"Couldn't remove node with value: ${value}")
      }
    })
  }
}