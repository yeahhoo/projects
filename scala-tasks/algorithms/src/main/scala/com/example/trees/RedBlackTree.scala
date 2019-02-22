package com.example.trees

import scala.util.Random
import com.example.trees.redblacktree._

/** a little hack to avoid interfering with node classes of other tree types. */
package com.example.trees.redblacktree {

  abstract class Node[+T]

  case object EmptyNode extends Node[Nothing]

  case class Tree[+T](
                       val value: T,
                       val blackHeight: Int,
                       val isBlack: Boolean,
                       val left: Node[T],
                       val right: Node[T]
                     ) extends Node[T] {
    // red node by default
    def this(value: T) = this(value, 0, false, EmptyNode, EmptyNode)
  }
}

/**
  * Red black tree implementation. Inspired by:
  * https://www.cs.usfca.edu/~galles/visualization/RedBlack.html
  * https://www.geeksforgeeks.org/red-black-tree-set-1-introduction-2/
  **/
class RedBlackTree[T <% Ordered[T]] {

  var root: Node[T] = EmptyNode

  private val DOUBLE_BLACK = -1

  /** Prints tree in human-readable format. */
  def printTree(): Unit = {
    /** returns List[(height, value, shift, true = isRight, true = isBlack)] */
    def _traverse(node: Node[T], h: Int, shift: Int, parentShift: Int): List[(Int, T, Int, Boolean, Boolean)] = {
      val delta = scala.math.abs(shift - parentShift) / 2
      val ls = shift - delta
      val rs = shift + delta
      node match {
        case EmptyNode => Nil
        case Tree(_, _, _, ln@Tree(_, _, _, _, _), rn@Tree(_, _, _, _, _)) => {
          ((h, ln.value, ls, false, ln.isBlack) ::
            (h, rn.value, rs, true, rn.isBlack) ::
            (_traverse(ln, h - 1, ls, shift) ++ _traverse(rn, h - 1, rs, shift)))
        }
        case Tree(_, _, _, ln@Tree(_, _, _, _, _), EmptyNode) =>
          (h, ln.value, ls, false, ln.isBlack) :: _traverse(ln, h - 1, ls, shift)
        case Tree(_, _, _, EmptyNode, rn@Tree(_, _, _, _, _)) =>
          (h, rn.value, rs, true, rn.isBlack) :: _traverse(rn, h - 1, rs, shift)
        case Tree(_, _, _, EmptyNode, EmptyNode) => Nil
      }
    }

    def _calcMaxSpan(node: Node[T]): Int = {
      def _findLongest(node: Node[T]): Int = node match {
        case EmptyNode => 0
        case Tree(v, _, _, l, r) => math.max(v.toString.length, math.max(_findLongest(l), _findLongest(r)))
      }
      math.pow(2, treeHeight(node) - 1).toInt * (_findLongest(node) + 2)
    }

    val height = treeHeight(root)
    val res = root match {
      case EmptyNode => {
        println("tree is empty")
        return ()
      }
      case Tree(value, _, _, _, _) => {
        val shift: Int = _calcMaxSpan(root) / 2
        ((height, value, shift, true, true) :: _traverse(root, height - 1, shift, 0))
          .sortWith((x, y) => if (x._1 == y._1) x._3 < y._3 else x._1 > y._1)
      }
    }

    val grouped = res.groupBy(_._1)
    (height to 1 by -1).toList.foreach(i => {
      val numberString = grouped(i).map({ case (l, v, s, _, ib) =>
        val isBlackFlag = if (ib) "b" else "r"
        ((List.fill(s)(" ") :+ (v.toString + isBlackFlag))).mkString("")
      }).reduceRight((x, y) => {
        val end = y.slice(x.length, y.length)
        x + end
      })
      val arrowString = grouped(i).map({ case (_, _, s, isRight, _) => {
        val arrow = if (isRight) "\\" (0) else "/"
        ((List.fill(s)(" ") :+ arrow)).mkString("")
      }
      }).reduceRight((x, y) => x + y.slice(x.length, y.length))
      println(arrowString)
      println(numberString)
    })
  }

  /** Checks whether given value is presented in the tree. */
  def contains(value: T): Boolean = {
    def _contains(node: Node[T], value: T): Boolean = node match {
      case EmptyNode => false
      case t@Tree(v, _, _, l, r) => {
        if (v == value) true
        else if (v > value) _contains(l, value)
        else _contains(r, value)
      }
    }

    _contains(root, value)
  }

  /** Inserts given value into the tree. */
  def insert(value: T): Node[T] = {

    def _insert(node: Node[T], value: T): Tree[T] = {
      val tree: Tree[T] = node match {
        case EmptyNode => return new Tree[T](value)
        case current@Tree(nodeValue, h, ib, left, right) => {
          if (value < nodeValue) {
            val leftNode = _insert(left, value)
            val newHeight = if (current.blackHeight == leftNode.blackHeight && leftNode.isBlack && ib) h + 1 else h
            new Tree(nodeValue, newHeight, ib, leftNode, right)
          } else {
            val rightNode = _insert(right, value)
            val newHeight = if (current.blackHeight == rightNode.blackHeight && rightNode.isBlack && ib) h + 1 else h
            new Tree(nodeValue, newHeight, ib, left, rightNode)
          }
        }
      }
      balanceInsert(tree)
    }

    val Tree(v, h, ib, l, r) = _insert(root, value)
    root = new Tree(v, if (h == 0) 1 else h, ib, l, r)
    if (!ib) {
      (l, r) match {
        case (EmptyNode, EmptyNode) => root = new Tree(v, 1, true, l, r)
        case (Tree(_, lh, _, _, _), EmptyNode) => root = new Tree(v, lh + 1, true, l, r)
        case (EmptyNode, Tree(_, rh, _, _, _)) => root = new Tree(v, rh + 1, true, l, r)
        case (Tree(_, _, _, _, _), Tree(_, rh, _, _, _)) => root = new Tree(v, rh + 1, true, l, r)
        case (_, _) => throw new IllegalStateException("Unexpected result for \"insert\" operation")
      }
    }

    root
  }

  private def balanceInsert(tree: Tree[T]): Tree[T] = {
    tree match {
      case b@Tree(v, bh, true, Tree(l_v1, l_h1, false, l_l1, l_r1), Tree(r_v1, r_h1, false, r_l1, r_r1)) => {
        if (isRed(l_l1) || isRed(l_r1) || isRed(r_l1) || isRed(r_r1))
          Tree(v, l_h1 + 1, false, Tree(l_v1, l_h1 + 1, true, l_l1, l_r1), Tree(r_v1, r_h1 + 1, true, r_l1, r_r1))
        else b
      }
      // left-left case (right-right turn)
      case g@Tree(gv, gh, true, p@Tree(pv, _, false, x@Tree(_, _, false, _, _), t3), u) => {
        new Tree(pv, gh, true, x, new Tree(gv, gh - 1, false, t3, u))
      }
      // left-right case (right-left turn)
      case g@Tree(gv, gh, true, p@Tree(pv, ph, false, t1, x@Tree(xv, _, false, t2, t3)), u) => {
        new Tree(xv, gh, true, new Tree(pv, ph, false, t1, t2), new Tree(gv, gh - 1, false, t3, u))
      }
      // right-right case (left-left turn)
      case g@Tree(gv, gh, true, u, p@Tree(pv, _, false, t3, x@Tree(_, _, false, _, _))) => {
        new Tree(pv, gh, true, new Tree(gv, gh - 1, false, u, t3), x)
      }
      // right-left case (left-right turn)
      case Tree(gv, gh, true, u, Tree(pv, ph, false, Tree(xv, _, false, t3, t4), t5)) => {
        new Tree(xv, gh, true, new Tree(gv, gh - 1, false, u, t3), new Tree(pv, ph, false, t4, t5))
      }
      case _ => tree
    }
  }

  /** Removes node from the tree by given value. */
  def delete(value: T): Boolean = {

    def _delete(node: Node[T], value: T): (Node[T], Boolean) = {
      val removedNode: (Node[T], Boolean) = node match {
        case EmptyNode => (EmptyNode, false)
        case t@Tree(v, bh, ib, EmptyNode, EmptyNode) => {
          if (v == value) (EmptyNode, true) else (EmptyNode, false)
        }
        case t@Tree(v, bh, ib, l@Tree(_, _, _, _, _), r@EmptyNode) => {
          if (v == value) if (ib && isRed(l)) (changeColour(l), true) else (l, true)
          else if (v < value) (EmptyNode, false)
          else {
            val (repNode, isRemoved) = _delete(l, value)
            if (isRemoved) (new Tree(v, bh, ib, repNode, r), isRemoved) else (repNode, isRemoved)
          }
        }
        case t@Tree(v, bh, ib, l@EmptyNode, r@Tree(_, _, _, _, _)) => {
          if (v == value) if (ib && isRed(r)) (changeColour(r), true) else (r, true)
          else if (v > value) (EmptyNode, false)
          else {
            val (repNode, isRemoved) = _delete(r, value)
            if (isRemoved) (new Tree(v, bh, ib, l, repNode), isRemoved) else (repNode, isRemoved)
          }
        }
        case t@Tree(v, bh, ib, l@Tree(_, _, _, _, _), r@Tree(_, _, _, _, _)) => {
          if (v > value) {
            val (newLeft, isRemoved) = _delete(l, value)
            if (isRemoved) (new Tree(v, bh, ib, newLeft, r), true) else (t, isRemoved)
          } else if (v < value) {
            val (newRight, isRemoved) = _delete(r, value)
            if (isRemoved) (new Tree(v, bh, ib, l, newRight), true) else (t, isRemoved)
          } else {
            def _max(subNode: Tree[T]): Tree[T] = subNode match {
              case Tree(_, _, _, _, EmptyNode) => subNode
              case Tree(_, _, _, _, r@Tree(_, _, _, _, _)) => _max(r)
              case Tree(_, _, _, _, _) => subNode
            }

            val repMaxNode = _max(l)
            val (newLeft, isRemoved) = _delete(l, repMaxNode.value)
            (new Tree(repMaxNode.value, bh, ib, newLeft, r), isRemoved)
          }
        }
      }
      removedNode._1 match {
        case EmptyNode => removedNode
        case t@Tree(_, _, _, _, _) => (balanceRemove(t), removedNode._2)
      }
    }

    root match {
      case EmptyNode => false
      case Tree(_, _, _, _, _) => {
        val (newNode, wasRemoved) = _delete(root, value)
        newNode match {
          case EmptyNode => root = EmptyNode
          case t@Tree(_, _, _, _, _) => if (isRed(t)) root = changeColour(t) else root = t
        }
        wasRemoved
      }
    }
  }

  private def balanceRemove(node: Tree[T]): Tree[T] = node match {
    case t@Tree(v, bh, ib, EmptyNode, EmptyNode) => {
      if (ib && bh != 1) new Tree(v, 1, ib, EmptyNode, EmptyNode)
      else if (!ib && bh != 0) new Tree(v, 0, ib, EmptyNode, EmptyNode)
      else t
    }
    case t@Tree(v, bh, ib, l@EmptyNode, r@Tree(rv, rbh, rib, rl, rr)) => {
      if (isRightChildBlack(t)) {
        if (isRed(rl)) return rightLeftTurn(t)
        else if (isRed(rr)) return leftTurn(t)
        else if (hasNoChildren(r)) return new Tree(v, rbh, true, l, changeColour(r))
      } else if (isRightChildRed(t)) {
        if (areBothChildrenBlack(r)) return leftTurn(t)
        else if (isRed(t)) return changeColour(t)
      }
      t
    }
    case t@Tree(v, bh, ib, l@Tree(lv, lbh, lib, ll, lr), r@EmptyNode) => {
      if (isLeftChildBlack(t)) {
        if (isRed(lr)) return leftRightTurn(t)
        else if (isRed(ll)) return rightTurn(t)
        else if (hasNoChildren(l)) return new Tree(v, lbh, true, changeColour(l), r)
      } else if (isLeftChildRed(t)) {
        if (areBothChildrenBlack(l)) return rightTurn(t)
        else if (isRed(t)) return changeColour(t)
      }
      t
    }
    case t@Tree(v, bh, ib, l@Tree(lv, lbh, lib, ll, lr), r@Tree(rv, rbh, rib, rl, rr)) => {

      def _checkHeightAndRedness(tree: Tree[T]): Tree[T] = {
        val Tree(v, bh, ib, l, b) = tree
        if (isRed(tree) && hasAtLeastOneRedChild(tree)) changeColour(tree)
        else if ((isBlack(tree) && bh != rbh + 1) || (isRed(tree) && bh != rbh)) {
          val nh = if (isBlack(tree)) rbh + 1 else rbh
          new Tree(v, nh, ib, l, r)
        }
        else tree
      }

      if (lbh < rbh) {
        if (isRed(l)) {
          if (isBlack(r) && hasNoRedChildren(r)) balanceRemove(new Tree(v, lbh + 1, ib, l, changeColour(r)))
          else balanceRemove(new Tree(v, if (ib) rbh + 1 else rbh, ib, changeColour(l), r))
        } else {
          if (isBlack(r) && hasNoRedChildren(r)) balanceRemove(new Tree(v, lbh + 1, true, l, changeColour(r)))
          else if ((isBlack(r) && isRightChildRed(r)) || isRed(r)) balanceRemove(leftTurn(t))
          else if (isBlack(r) && isRightChildBlack(r)) balanceRemove(rightLeftTurn(t))
          else _checkHeightAndRedness(t)
        }
      } else if (lbh > rbh) {
        if (isRed(r)) {
          if (isBlack(l) && hasNoRedChildren(l)) balanceRemove(new Tree(v, rbh + 1, ib, changeColour(l), r))
          else balanceRemove(new Tree(v, if (ib) lbh + 1 else lbh, ib, l, changeColour(r)))
        } else {
          if (isBlack(l) && hasNoRedChildren(l)) balanceRemove(new Tree(v, rbh + 1, true, changeColour(l), r))
          else if ((areBothChildrenBlack(t) && isLeftChildRed(l)) || isRed(l)) balanceRemove(rightTurn(t))
          else if (isBlack(l) && isLeftChildBlack(l)) balanceRemove(leftRightTurn(t))
          else _checkHeightAndRedness(t)
        }
      } else _checkHeightAndRedness(t)
    }
    case Tree(_, _, _, _, _) => throw new IllegalStateException("Unexpected state for balancing tree after delete")
  }

  private def leftTurn(tree: Tree[T]): Tree[T] = tree match {
    case Tree(v, bh, ib, l, Tree(rv, rbh, rib, rl, rr)) => {
      val newLeft = balanceRemove(new Tree(v, DOUBLE_BLACK, ib, l, rl))
      balanceRemove(new Tree(rv, DOUBLE_BLACK, rib, newLeft, rr))
    }
    case _ => throw new IllegalArgumentException("Given argument doesn't satisfy condition for left turn")
  }

  private def leftRightTurn(tree: Tree[T]): Tree[T] = tree match {
    case Tree(v, bh, ib, l@Tree(lv, lbh, lib, ll, lr), r) => {
      balanceRemove(rightTurn(new Tree(v, bh, ib, leftTurn(l), r)))
    }
    case _ => throw new IllegalArgumentException("Given argument doesn't satisfy condition for left-right turn")
  }

  private def rightTurn(tree: Tree[T]): Tree[T] = tree match {
    case Tree(v, bh, ib, Tree(lv, lbh, lib, ll, lr), r) => {
      val newRight = balanceRemove(new Tree(v, DOUBLE_BLACK, ib, lr, r))
      balanceRemove(new Tree(lv, DOUBLE_BLACK, lib, ll, newRight))
    }
    case _ => throw new IllegalArgumentException("Given argument doesn't satisfy condition for right turn")
  }

  private def rightLeftTurn(tree: Tree[T]): Tree[T] = tree match {
    case Tree(v, bh, ib, l, r@Tree(rv, rbh, rib, rl, rr)) => {
      balanceRemove(leftTurn(new Tree(v, bh, ib, l, rightTurn(r))))
    }
    case _ => throw new IllegalArgumentException("Given argument doesn't satisfy condition for right-left turn")
  }

  /** Helper handy function that alarms if tree gets out of balance. */
  private[trees] def isBalanced: Boolean = isBalanced(root)

  private[trees] def isBalanced(node: Node[T]): Boolean = node match {
    case EmptyNode => true
    case t@Tree(v, bh, ib, l@EmptyNode, r@EmptyNode) => {
      if (ib && bh != 1) false
      else if (!ib && bh != 0) false
      else true
    }
    case t@Tree(v, bh, ib, l@Tree(lt, lbh, lib, ll, lr), r@EmptyNode) => {
      if (ib && lib) false
      else if (ib && !lib && (bh - lbh != 1 || lbh >= 1)) false
      else if (!ib && lib && bh == lbh) false
      else isBalanced(l)
    }
    case t@Tree(v, bh, ib, l@EmptyNode, r@Tree(rv, rbh, rib, rl, rr)) => {
      if (ib && rib) false
      else if (ib && !rib && (bh - rbh != 1 || rbh >= 1)) false
      else if (!ib && rib && bh == rbh) false
      else isBalanced(r)
    }
    case t@Tree(v, bh, ib, l@Tree(lv, lbh, lib, ll, lr), r@Tree(rv, rbh, rib, rl, rr)) => {
      if (ib && (lbh != rbh || bh - lbh != 1)) false
      else if (!ib && (bh != lbh || bh != rbh || !lib || !rib)) false
      else isBalanced(l) && isBalanced(r)
    }
  }

  /** Returns the height of the tree consisting of black nodes only */
  private def height(node: Node[T]): Int = node match {
    case EmptyNode => 0
    case Tree(_, h, _, _, _) => h
  }

  private def changeColour(node: Tree[T]): Tree[T] = node match {
    case Tree(v, bh, ib, l, r) => new Tree(v, if (ib) height(r) else height(r) + 1, !ib, l, r)
  }

  /** Returns the height of the tree consisting of black and red nodes */
  private def treeHeight(node: Node[T]): Int = node match {
    case EmptyNode => 0
    case Tree(_, _, _, l, r) => {
      val lh = treeHeight(l)
      val rh = treeHeight(r)
      if (lh > rh) lh + 1 else rh + 1
    }
  }

  private def isEmpty(tree: Node[T]): Boolean = {
    tree.isInstanceOf[EmptyNode.type]
  }

  private def isBlack(tree: Node[T]): Boolean = {
    tree.isInstanceOf[Tree[T]] && tree.asInstanceOf[Tree[T]].isBlack
  }

  private def isRed(tree: Node[T]): Boolean = {
    tree.isInstanceOf[Tree[T]] && !tree.asInstanceOf[Tree[T]].isBlack
  }

  private def hasNoChildren(node: Node[T]): Boolean = node match {
    case EmptyNode => false
    case Tree(_, _, _, l, r) => isEmpty(l) && isEmpty(r)
  }

  private def isLeftChildBlack(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, l, _) => isBlack(l)
  }

  private def isLeftChildRed(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, l, _) => isRed(l)
  }

  private def isRightChildBlack(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, _, r) => isBlack(r)
  }

  private def isRightChildRed(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, _, r) => isRed(r)
  }

  private def areBothChildrenBlack(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, l, r) => isBlack(l) && isBlack(r)
  }

  private def areBothChildrenRed(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, l, r) => isRed(l) && isRed(r)
  }

  private def hasNoRedChildren(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, l, r) => !isRed(l) && !isRed(r)
  }

  private def hasAtLeastOneRedChild(node: Tree[T]): Boolean = node match {
    case Tree(_, _, _, l, r) => isRed(l) || isRed(r)
  }
}

object RedBlackTree {

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
    println(s"----- Red Black Tree -------")
    val tree = new RedBlackTree[Int]()

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
      if (!tree.delete(value) || !tree.isBalanced) {
        throw new RuntimeException(s"Couldn't remove node with value: ${value}")
      }
    })
  }
}