import org.scalatest._
import com.example.trees.Trie

class TrieTest extends FlatSpec {

  "Trie" should "correctly insert and find words" in {
    val trie = new Trie[Char]
    trie.insert("this".toList)
    trie.insert("is".toList)
    trie.insert("simple".toList)
    trie.insert("sentence".toList)

    assert(trie.contains("this".toList))
    assert(trie.contains("is".toList))
    assert(trie.contains("simple".toList))
    assert(trie.contains("sentence".toList))
    assert(!trie.contains("a".toList))
    assert(!trie.contains("word".toList))
    assert(trie.containsAsSubWord("simp".toList))
    assert(!trie.containsAsSubWord("semp".toList))
  }

  "Trie" should "correctly distinguish words with the same beginnings" in {
    val trie = new Trie[Char]
    trie.insert("aaaa".toList)
    trie.insert("a".toList)
    trie.insert("abc".toList)

    assert(trie.contains("a".toList))
    assert(trie.contains("aaaa".toList))
    assert(!trie.contains("aa".toList))
    assert(!trie.contains("aaa".toList))
    assert(!trie.contains("abb".toList))
    assert(trie.containsAsSubWord("aa".toList))
  }

  "Trie" should "correctly remove words with the same beginnings" in {
    val trie = new Trie[Char]
    trie.insert("keys".toList)
    trie.insert("key".toList)
    trie.delete("keys".toList)

    assert(!trie.contains("keys".toList))
    assert(!trie.containsAsSubWord("keys".toList))
    assert(trie.contains("key".toList))
  }

  "Trie" should "correctly remove words" in {
    val trie = new Trie[Char]
    val words = List("abc", "abb", "cda", "aaa", "aa")
    words.foreach(word => if (!trie.insert(word.toList)) {
      fail(s"couldn't insert the word ${word}")
    })

    words.foreach(word => if (!trie.contains(word.toList)) {
      fail(s"couldn't find the word ${word}")
    })

    trie.delete("aa".toList)

    assert(trie.contains("aaa".toList))
    assert(!trie.contains("a".toList))
    assert(trie.contains("abb".toList))
    assert(!trie.contains("aa".toList))
  }

  "Trie" should "correctly process text" in {
    val sentence = "In computer science, a trie, also called digital tree, radix tree or prefix tree, " +
      "is a kind of search tree—an ordered tree data structure used to store a dynamic set " +
      "or associative array where the keys are usually strings. Unlike a binary search tree, " +
      "no node in the tree stores the key associated with that node; instead, " +
      "its position in the tree defines the key with which it is associated. " +
      "All the descendants of a node have a common prefix of the string associated with that node, " +
      "and the root is associated with the empty string. Keys tend to be associated with leaves, " +
      "though some inner nodes may correspond to keys of interest. Hence, keys are not necessarily " +
      "associated with every node. For the space-optimized presentation of prefix tree, " +
      "see compact prefix tree."
    val words = sentence.split(" ").distinct.toList.map(word => word.toCharArray.toList)

    testText(words)
  }

  def testText[T](words: List[List[T]]): Unit = {
    val trie = new Trie[T]
    words.foreach(word => if (!trie.insert(word)) fail(s"trie couldn't insert the word: ${word}"))
    words.foreach(word => {
      if (!trie.contains(word)) fail(s"Trie doesn't contain word: ${word}")
      if (!trie.delete(word)) fail(s"Trie didn't remove the word: ${word}")
      if (trie.contains(word)) fail(s"Trie contains word after it was removed: ${word}")
    })
    assert(trie.isEmpty())
  }
}
