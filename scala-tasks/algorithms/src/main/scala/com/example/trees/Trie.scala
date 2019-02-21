package com.example.trees

import com.example.trees.trie._

/** a little hack to avoid interfering with node classes of other tree types. */
package com.example.trees.trie {

  class Node[T](val map: Map[T, Node[T]], val isWord: Boolean) {

    def this(isWord: Boolean) = this(Map.empty[T, Node[T]], isWord)

    def this() = this(false)
  }
}

/** Trie implementation. */
class Trie[T] {

  var root = new Node[T]()

  /** Inserts given value into the trie. */
  def insert(items: List[T]): Boolean = {

    def _insert(node: Node[T], items: List[T]): (Node[T], Boolean) = {
      items match {
        case Nil => (new Node(node.map, true), true)
        case x :: xs => {
          node.map.get(x) match {
            case None => {
              // adding new item to the trie hierarchy
              val (newNode, isAdded) = _insert(new Node(), xs)
              (new Node(node.map + (x -> newNode), node.isWord), isAdded)
            }
            case Some(nextNode) => {
              val (newNode, isAdded) = _insert(nextNode, xs)
              if (isAdded) {
                // merging in to existing map if something was added
                (new Node(node.map + (x -> newNode), node.isWord), isAdded)
              } else (newNode, isAdded)
            }
          }
        }
      }
    }

    items match {
      case Nil => false
      case _ => {
        val (newRoot, isAdded) = _insert(root, items)
        root = newRoot
        isAdded
      }
    }
  }

  /** Checks whether given value is presented in the tree by full match. */
  def contains(items: List[T]): Boolean = {
    contains(root, items, true)
  }

  /**
    * Checks whether given value is presented in the tree by particular match.
    * It means that returns true even if given value is just a subset of another item.
    * Simple speaking strict full match is not mandatory.
    * */
  def containsAsSubWord(items: List[T]): Boolean = {
    contains(root, items, false)
  }

  private def contains(node: Node[T], items: List[T], isFullMatch: Boolean): Boolean = items match {
    case Nil => node.isWord | !isFullMatch
    case x :: xs => node.map.get(x) match {
      case None => false
      case Some(nextNode) => contains(nextNode, xs, isFullMatch)
    }
  }

  /** Removes given node out of the trie. */
  def delete(items: List[T]): Boolean = {

    def _delete(node: Node[T], items: List[T]): (Node[T], Boolean) = items match {
      case Nil => {
        if (node.isWord) {
          if (node.map.isEmpty) (new Node(false), true) else (new Node(node.map, false), true)
        } else (node, false)
      }
      case x :: xs => node.map.get(x) match {
        case None => {
          (node, false)
        }
        case Some(nextNode) => {
          val (removedNode, isRemoved) = _delete(nextNode, xs)
          if (isRemoved) {
            if (removedNode.map.isEmpty && !removedNode.isWord) {
              (new Node(node.map - x, node.isWord), isRemoved)
            } else {
              (new Node(node.map + (x -> removedNode), node.isWord), isRemoved)
            }
          } else (removedNode, isRemoved)
        }
      }
    }

    val (newNode, isRemoved) = _delete(root, items)
    if (isRemoved) root = newNode
    isRemoved
  }

  /** Checks if trie is empty. */
  def isEmpty(): Boolean = root.map.isEmpty && !root.isWord
}
