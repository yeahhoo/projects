import org.scalatest._
import com.example.utils.TestUtil
import com.example.trees.AvlTree

class AvlTreeTest extends FlatSpec {

  "AvlTree" should "correctly insert and then remove 10 random items" in {
    testItems(TestUtil.shuffleList((1 to 10 by 1).toList))
  }

  "AvlTree" should "correctly insert and then remove 40 random items" in {
    testItems(TestUtil.shuffleList((1 to 40 by 1).toList))
  }

  "AvlTree" should "correctly insert and then remove 100 random items" in {
    testItems(TestUtil.shuffleList((1 to 100 by 1).toList))
  }

  "AvlTree" should "correctly insert and then remove 1000 random items" in {
    testItems(TestUtil.shuffleList((1 to 1000 by 1).toList))
  }

  "AvlTree" should "correctly insert and then remove 10000 random items" in {
    testItems(TestUtil.shuffleList((1 to 10000 by 1).toList))
  }

  private def testItems[T <% Ordered[T]](set: List[T]): Unit = {
    val tree = set.foldLeft(AvlTree[T]())((t, value) => {
      val (newTree, isAdded) = t.insert(value)
      if (!isAdded) fail(s"Couldn't insert value: ${value} ")
      newTree
    })

    assert(!tree.isEmpty())
    set.foreach(value => if (!tree.contains(value)) fail(s"Couldn't find node with value: ${value}"))

    val newTree = TestUtil.shuffleList(set).foldLeft(tree)((t, value) => {
      val (newTree, isRemoved) = t.delete(value)
      if (!isRemoved) fail(s"Couldn't remove node with value: ${value}")
      newTree
    })
    assert(newTree.isEmpty())
  }
}