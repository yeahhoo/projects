import org.scalatest._
import com.example.utils.TestUtil
import com.example.trees.RedBlackTree

class RedBlackTreeTest extends FlatSpec {

  "RedBlackTree" should "correctly insert and then remove 10 random items" in {
    testItems(TestUtil.shuffleList((1 to 10 by 1).toList))
  }

  "RedBlackTree" should "correctly insert and then remove 40 random items" in {
    testItems(TestUtil.shuffleList((1 to 40 by 1).toList))
  }

  "RedBlackTree" should "correctly insert and then remove 100 random items" in {
    testItems(TestUtil.shuffleList((1 to 100 by 1).toList))
  }

  "RedBlackTree" should "correctly insert and then remove 1000 random items" in {
    testItems(TestUtil.shuffleList((1 to 1000 by 1).toList))
  }

  "RedBlackTree" should "correctly insert and then remove 10000 random items" in {
    testItems(TestUtil.shuffleList((1 to 10000 by 1).toList))
  }

  "RedBlackTree" should "correctly insert and then remove prepared data set 1" in {
    testItems(
      List(1, 31, 30, 21, 36, 14, 4, 39, 15, 11, 25, 12, 6, 16, 20, 24, 27, 35, 37, 40, 13, 19, 26, 28, 18, 8, 22, 3, 33, 32, 2, 7, 9, 38, 23, 34, 17, 29, 10, 5),
      List(16, 40, 10, 23, 17, 20, 30, 15, 21, 37, 27, 36, 3, 24, 13, 14, 11, 22, 35, 18, 31, 34, 39, 29, 26, 9, 12, 32, 2, 8, 6, 4, 19, 28, 38, 7, 25, 5, 1, 33)
    )
  }

  "RedBlackTree" should "correctly insert and then remove prepared data set 2" in {
    testItems(
      List(15, 2, 5, 14, 7, 3, 6, 12, 11, 9, 13, 4, 10, 1, 8),
      List(1, 11, 13, 14, 10, 6, 7, 15, 2, 8, 5, 3, 9, 4, 12)
    )
  }

  "RedBlackTree" should "correctly insert and then remove prepared data set 3" in {
    testItems(
      List(15, 14, 6, 8, 3, 11, 9, 12, 10, 5, 4, 7, 1, 13, 2),
      List(11, 4, 10, 2, 15, 5, 3, 13, 14, 1, 12, 7, 8, 6, 9)
    )
  }

  "RedBlackTree" should "correctly insert and then remove prepared data set 4" in {
    testItems(
      List(15, 14, 6, 8, 3, 11, 9, 12, 10, 5, 4, 7, 1, 13, 2),
      List(11, 4, 10, 2, 15, 13, 3, 5, 14, 1, 12, 7, 8, 6, 9)
    )
  }

  "RedBlackTree" should "correctly insert and then remove prepared data set 5" in {
    testItems(
      List(3, 12, 16, 4, 20, 10, 2, 11, 15, 9, 1, 8, 14, 19, 13, 7, 6, 18, 5, 17),
      List(16, 8, 4, 20, 10, 14, 3, 1, 7, 6, 11, 13, 15, 5, 19, 18, 12, 17, 2, 9)
    )
  }


  "RedBlackTree" should "correctly insert and then remove prepared data set 6" in {
    testItems(
      List(1, 3, 4, 16, 7, 2, 10, 5, 12, 19, 20, 15, 8, 14, 13, 6, 9, 18, 11, 17),
      List(14, 4, 17, 5, 20, 10, 7, 12, 2, 18, 13, 16, 11, 15, 19, 6, 9, 3, 8, 1)
    )
  }

  "RedBlackTree" should "correctly insert and then remove prepared data set 7" in {
    testItems(
      List(20, 15, 18, 16, 6, 9, 10, 19, 13, 2, 5, 3, 7, 11, 1, 14, 4, 12, 17, 8),
      List(3, 11, 7, 14, 5, 18, 17, 15, 13, 20, 1, 6, 12, 16, 9, 19, 10, 4, 2, 8)
    )
  }

  "RedBlackTree" should "correctly insert and then remove prepared data set 8" in {
    testItems(
      List(19, 11, 1, 16, 12, 20, 6, 10, 8, 3, 18, 13, 5, 7, 14, 4, 15, 17, 9, 2),
      List(1, 20, 15, 6, 9, 7, 17, 3, 19, 5, 2, 4, 16, 10, 13, 18, 11, 12, 8, 14)
    )
  }

  private def testItems[T <% Ordered[T]](set: List[T], removeSet: List[T] = Nil): Unit = {
    val tree = set.foldLeft(RedBlackTree[T]())((t, i) => {
      val (newTree, isAdded) = t.insert(i)
      if (!isAdded) throw new RuntimeException(s"couldn't insert item: ${i}")
      newTree
    })

    assert(!tree.isEmpty())
    set.foreach(value => if (!tree.contains(value)) fail(s"Couldn't find node with value: ${value}"))

    val setToRemove = if (removeSet == Nil) TestUtil.shuffleList(set) else removeSet
    val newTree = setToRemove.foldLeft(tree)((t, value) => {
      val (newTree, isRemoved) = t.delete(value)
      if (!isRemoved) {
        throw new RuntimeException(s"Couldn't remove node with value: ${value}")
      }
      newTree
    })
    assert(newTree.isEmpty())
  }
}