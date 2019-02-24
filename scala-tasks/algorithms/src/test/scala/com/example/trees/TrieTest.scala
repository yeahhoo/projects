import org.scalatest._
import com.example.trees.Trie

class TrieTest extends FlatSpec {

  "Trie" should "correctly insert and find words" in {
    val trie = Trie[Char](List("this", "is", "simple", "sentence").map(_.toList))

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
    val trie = Trie[Char](List("aaaa", "a", "abc").map(_.toList))

    assert(trie.contains("a".toList))
    assert(trie.contains("aaaa".toList))
    assert(!trie.contains("aa".toList))
    assert(!trie.contains("aaa".toList))
    assert(!trie.contains("abb".toList))
    assert(trie.containsAsSubWord("aa".toList))
  }

  "Trie" should "correctly remove words with the same beginnings" in {
    val trie = Trie[Char](List("keys", "key").map(_.toList)).delete("keys".toList)._1

    assert(!trie.contains("keys".toList))
    assert(!trie.containsAsSubWord("keys".toList))
    assert(trie.contains("key".toList))
  }

  "Trie" should "correctly remove words" in {
    val words = List("abc", "abb", "cda", "aaa", "aa")
    val trie = words.foldLeft(Trie[Char](List.empty))((t, word) => {
      val (newTree, isAdded) = t.insert(word.toList)
      if (!isAdded) fail(s"couldn't insert the word ${word}")
      newTree
    })

    words.foreach(word => if (!trie.contains(word.toList)) {
      fail(s"couldn't find the word ${word}")
    })

    val resTrie = trie.delete("aa".toList)._1

    assert(resTrie.contains("aaa".toList))
    assert(!resTrie.contains("a".toList))
    assert(resTrie.contains("abb".toList))
    assert(!resTrie.contains("aa".toList))
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
    val trie = words.foldLeft(Trie[T](List.empty))((t, word) => {
      val (newTree, isAdded) = t.insert(word)
      if (!isAdded) fail(s"trie couldn't insert the word: ${word}")
      newTree
    })


    val resTrie = words.foldLeft(trie)((t, word) => {
      if (!t.contains(word)) fail(s"Trie doesn't contain word: ${word}")
      val (newTree, isRemoved) = t.delete(word)
      if (!isRemoved) fail(s"Trie didn't remove the word: ${word}")
      if (newTree.contains(word)) fail(s"Trie contains word after it was removed: ${word}")
      newTree
    })
    assert(resTrie.isEmpty())
  }
}
