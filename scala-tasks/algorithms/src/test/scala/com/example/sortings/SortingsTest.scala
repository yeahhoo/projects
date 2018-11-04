import org.scalatest._
import com.example.utils.TestUtil
import com.example.sortings.Sortings

class SortingsTest extends FlatSpec {

  it should "sort random list in ascending order with bubble sort algorithm" in {
    val testList = TestUtil.shuffleList((1 to 10 by 1).toList)
    assert(isListSorted(Sortings.bubbleSort(testList)))
  }

  it should "sort random list in ascending order with merge sort algorithm" in {
    val testList = TestUtil.shuffleList((1 to 10 by 1).toList)
    assert(isListSorted(Sortings.mergeSort(testList)))
  }

  it should "sort random list in ascending order with insert sort algorithm" in {
    val testList = TestUtil.shuffleList((1 to 10 by 1).toList)
    assert(isListSorted(Sortings.insertSort(testList)))
  }

  it should "sort random list in ascending order with quick sort algorithm" in {
    val testList = TestUtil.shuffleList((1 to 10 by 1).toList)
    assert(isListSorted(Sortings.quickSort(testList)))
  }

  private def isListSorted[T <% Ordered[T]](list: List[T]): Boolean = {
    list match {
      case Nil => true
      case x :: Nil => true
      case x :: y :: xs => if (x <= y) isListSorted(y :: xs) else false
    }
  }
}