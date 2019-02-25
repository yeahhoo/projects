import com.example.trees.Heap
import org.scalatest._
import com.example.utils.TestUtil

class HeapTest extends FlatSpec {

  "Heap" should "correctly insert 10 random items" in {
    testItems(TestUtil.shuffleList((1 to 10 by 1).toList))
  }

  "Heap" should "correctly insert 1000 random items" in {
    testItems(TestUtil.shuffleList((1 to 1000 by 1).toList))
  }

  "Heap" should "correctly insert 10000 random items" in {
    testItems(TestUtil.shuffleList((1 to 10000 by 1).toList))
  }

  def testItems[T <% Ordered[T]](list: Seq[T]): Unit = {
    assert(TestUtil.isListSorted(Heap[T](list:_*).unfold().reverse))
  }
}
