package org.alecuba16
import scala.annotation.tailrec

/*
  Case class to hold node data
  In a trie like structure we should have the number of occurrences for the current accum string (until the current
  node) and also the different next possible values, that in this case is the alphabet 0-9 A-Z a-z. I have set the
  alphabet size to the difference between the ascii code z - 0, which covers at least the 0-9 A-Z a-Z and some
  punctuation characters. I have not checked with corners cases!.

  A possible calculation is the size of the alphabet (a) * avg word length (w) * number of words (N), O(a*w*N) -> O(N).
  Time complexity search is O(1), because we can get the result in one pass, insertion is O(N) because we have to
  visit N nodes.

  Consulted info for the code: https://en.wikipedia.org/wiki/Trie
 */
case class TrieDict(occurrences: Option[Int], child: List[Option[TrieDict]]) {
  def add(word: String, occurrences: Int): TrieDict = TrieDict.addNodeRec(this, word, occurrences, 0)
  def find(word: String): Option[Int] = TrieDict.findNodeRec(this, word, 0)
  def numWords():Integer = TrieDict.numWords
}

// Companion Object
object TrieDict {
  private val Char0 = '0'.toInt
  private val AlphabetSize = 'z'.toInt - Char0+1
  private var numWords = 0

  // Helper for generating empty Nodes
  def emptyNode: TrieDict = new TrieDict(None, List.fill(AlphabetSize)(None))

  // By default return an emptyNode (the root)
  def apply: TrieDict = emptyNode

  @tailrec
  private def findNodeRec(node: TrieDict, word: String, charPos: Int): Option[Int] = {
    // Base case
    if (word.length == charPos) {
      return node.occurrences
    }

    val childIndex = word.charAt(charPos) - Char0
    node.child(childIndex) match {
      // Empty child
      case None => None
      // The child has a child
      case Some(nextNode) => findNodeRec(nextNode, word, charPos + 1)
    }
  }


  private def addNodeRec(node: TrieDict, word: String, occurrences: Int, charPos: Int): TrieDict = {
    // Base case we have processed the string
    if (word.length == charPos) {
      numWords += 1
      return node.copy(occurrences = Some(occurrences))
    }
    // Rec case
    // From char to number
    val childIndexKey    = word.charAt(charPos) - Char0
    // Create empty child
    val nextItem = node.child(childIndexKey).getOrElse(TrieDict.emptyNode)
    // Continue generating the following chars in a next call
    val newTreeNode  = addNodeRec(nextItem, word, occurrences, charPos + 1)
    // Add the new tree to the current child position for the current word, it could be
    val newNext  = node.child.updated(childIndexKey, Some(newTreeNode))
    // Clone the current node, but with the new child tree
    node.copy(child = newNext)
  }
}