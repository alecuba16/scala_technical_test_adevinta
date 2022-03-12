package org.alecuba16
import scala.annotation.tailrec

/*
  Case class to hold node data
  In a trie like structure we should have the number of occurrences for the current accum string (until the current
  node) and also the different next possible values, that in this case is the alphabet 0-9 A-Z a-z. I have set the
  alphabet size to the difference between the ascii code z - 0, which covers at least the 0-9 A-Z a-Z and some
  punctuation characters. I have not checked with corners cases!.

  A possible calculation is the size of the alphabet (a) * avg word length (w) * number of words (N), in terms of 
  space O(a*w*N). I terms of time complexity is in the best case O(1) for one char and O(N) for the longest word.
  So it will be the avg of the word length.

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
    // Get the current char if defined (if there is a tree) or create a new subtree if there is no char
    val childSubTree = node.child(childIndexKey).getOrElse(TrieDict.emptyNode)
    // Continue visiting/generating the following chars in a next calls
    val updatedChildSubTree  = addNodeRec(childSubTree, word, occurrences, charPos + 1)
    // update the current child at the current char pos with the returned subtree
    val updatedChild  = node.child.updated(childIndexKey, Some(updatedChildSubTree))
    // Clone the current node, but change the old children with the new ones
    node.copy(child = updatedChild)
  }
}
