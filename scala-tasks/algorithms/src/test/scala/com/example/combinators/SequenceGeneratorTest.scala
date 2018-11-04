import org.scalatest._
import akka.util.Timeout
import scala.concurrent.duration._
import com.example.combinators.SequenceGenerator

class SequenceGeneratorTest extends FlatSpec {

  "SequenceGenerator" should "correctly generate combinations for given alphabet on given interval" in {
    val res = SequenceGenerator.generateCombsOnInterval(List(1, 2), List(2, 1, 1), List(1, 2))
    assert(res == List(List(2, 2), List(1, 1, 1), List(2, 1, 1)))
  }

  "SequenceGenerator" should "calculate number of combinations on given interval" in {
    assert(SequenceGenerator.combinationsCount(List(1, 2), List(1, 2, 3)) == 7)
  }

  "SequenceGenerator" should "calculate state after shifting given combination n times" in {
    assert(SequenceGenerator.calculateShift(List(1, 2, 3), 9) == List(3, 2))
  }

  "SequenceGenerator" should "calculate combinations for alphabet 1" in {
    val res = SequenceGenerator.seqCombinations(List(1, 2, 3, 4), 5, 4, 10, Timeout(30 seconds))
    assert(res.length == 1364)
    assert(res.head == List(1))
    assert(res.last == List(4, 4, 4, 4, 4))
  }

  "SequenceGenerator" should "calculate combinations for alphabet 2" in {
    val res = SequenceGenerator.seqCombinations(List(1, 2, 3), 3, 2, 5, Timeout(5 seconds))
    assert(res.length == 39)
    assert(res.head == List(1))
    assert(res.last == List(3, 3, 3))
  }
}