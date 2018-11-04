import org.scalatest._
import com.example.combinators.Combinator

class CombinatorTest extends FlatSpec {

  "Combinator" should "correctly generate combinations for given alphabet" in {
    val res = Combinator.combinations(List(1, 2, 3))
    assert(res == List(List(1), List(2), List(3), List(2, 3), List(1, 2), List(1, 3), List(1, 2, 3)))
  }

  "Combinator" should "correctly generate combinations of given size for given alphabet" in {
    val res = Combinator.combinations(List(1, 2, 3, 4), 2)
    assert(res == List(List(1, 2), List(1, 3), List(1, 4), List(2, 3), List(2, 4), List(3, 4)))
  }

  "Combinator" should "correctly generate variations for given alphabet" in {
    val res = Combinator.variations(List(1, 2, 3), 2)
    assert(res == List(List(1, 2), List(1, 3), List(2, 1), List(2, 3), List(3, 1), List(3, 2)))
  }

  "Combinator" should "correctly generate permutations for given alphabet" in {
    val res = Combinator.permutations(List(1, 2, 3))
    assert(res == List(List(1, 2, 3), List(1, 3, 2), List(2, 1, 3), List(2, 3, 1), List(3, 1, 2), List(3, 2, 1)))
  }

  "Combinator" should "correctly generate subsets of given size for specified alphabet" in {
    val res = Combinator.subsets(List(1, 2, 3, 4), 2)
    assert(res == List(List(1), List(1, 2), List(1, 3), List(1, 4), List(2), List(2, 1), List(2, 3), List(2, 4),
      List(3), List(3, 1), List(3, 2), List(3, 4), List(4), List(4, 1), List(4, 2), List(4, 3)))
  }

  "Combinator" should "correctly generate subsets with repeats for specified alphabet" in {
    val res = Combinator.subsetsWithRepeats("ab".toList)
    assert(res == List(List('a'), List('b'), List('a', 'a'), List('a', 'b'), List('b', 'a'), List('b', 'b')))
  }

  "Combinator" should "correctly generate combination sets of given size for specified alphabet" in {
    val res = Combinator.comboSets(List(0, 1, 2), 2)
    assert(res == List(List(0, 0), List(0, 1), List(0, 2), List(1, 0), List(1, 1), List(1, 2), List(2, 0), List(2, 1), List(2, 2)))
  }
}