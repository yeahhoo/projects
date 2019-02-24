import org.scalatest._
import com.example.utils.TestUtil
import com.example.trees.BTree

class BTreeTest extends FlatSpec {

  "BTree(3)" should "correctly insert given list of items" in {
    val data = List(10, 20, 30, 15, 40, 50, 35, 45, 55, 60, 70, 46, 47, 17, 19, 37, 48, 5)

    testInsertItems(3, data)
  }

  "BTree(5)" should "correctly insert given list of items" in {
    val data = List(10, 20, 30, 15, 40, 50, 35, 45, 55, 60, 70, 46, 47, 17, 19, 37, 48, 5)

    testInsertItems(5, data)
  }

  "BTree(3)" should "correctly insert random list of items of size 100" in {
    testInsertItems(3, TestUtil.shuffleList((1 to 100 by 1).toList))
  }


  "BTree(9)" should "correctly insert random list of items of size 1000" in {
    testInsertItems(9, TestUtil.shuffleList((1 to 1000 by 1).toList))
  }

  "BTree(3)" should "correctly insert and then remove prepared data set 1" in {
    testItems(
      3,
      List(10, 20, 30, 15, 40, 50, 35, 45, 55, 60, 70, 46, 47, 17, 19, 37, 48, 5, 80, 90, 100, 74),
      List(37, 48, 50, 70, 60, 46, 40, 47, 35, 20, 17, 19, 80, 45, 90, 100, 15, 30, 5, 10, 74, 55)
    )
  }

  "BTree(5)" should "correctly insert and then remove prepared data set 2" in {
    testItems(
      5,
      List(10, 20, 30, 15, 40, 50, 35, 45, 55, 60, 70, 46, 47, 17, 19, 37, 48, 5, 80, 90, 100, 74),
      List(37, 48, 50, 70, 60, 46, 40, 47, 35, 20, 17, 19, 80, 45, 90, 100, 15, 30, 5, 10, 74, 55)
    )
  }

  "BTree(3)" should "correctly insert and then remove 100 random items" in {
    testItems(3, TestUtil.shuffleList((1 to 100 by 1).toList))
  }

  "BTree(7)" should "correctly insert and then remove 100 random items" in {
    testItems(7, TestUtil.shuffleList((1 to 100 by 1).toList))
  }

  "BTree(4)" should "correctly insert and then remove 1000 random items" in {
    testItems(4, TestUtil.shuffleList((1 to 1000 by 1).toList))
  }

  "BTree(8)" should "correctly insert and then remove 1000 random items" in {
    testItems(8, TestUtil.shuffleList((1 to 1000 by 1).toList))
  }

  "BTree(3)" should "correctly insert and then remove 10000 random items" in {
    testItems(3, TestUtil.shuffleList((1 to 10000 by 1).toList))
  }

  "BTree(128)" should "correctly insert and then remove 10000 random items" in {
    testItems(128, TestUtil.shuffleList((1 to 10000 by 1).toList))
  }

  private def testInsertItems[T <% Ordered[T]](degree: Int, set: List[T]): Unit = {
    val tree = set.foldLeft(BTree[T](degree))((t, i) => {
      val (newTree, isAdded) = t.insert(i)
      if (!isAdded) fail(s"Couldn't insert value: ${i}")
      newTree
    })
    assert(!tree.isEmpty())
    set.foreach(value => if (!tree.contains(value)) fail(s"Couldn't find node with value: ${value}"))
  }

  private def testItems[T <% Ordered[T]](degree: Int, set: List[T], removeSet: List[T] = Nil): Unit = {
    val tree = set.foldLeft(BTree[T](degree))((t, i) => {
      val (newTree, isAdded) = t.insert(i)
      if (!isAdded) fail(s"Couldn't insert value: ${i}")
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
