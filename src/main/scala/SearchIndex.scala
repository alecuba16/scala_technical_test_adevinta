package org.alecuba16

import java.io.File
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class SearchIndex(private val numTopN: Integer,private val fileProcessor:FileProcessor) {
  private val dictionaries = mutable.Map[String, TrieDict]()

  def indexFiles(path: File): Try[Int] = {
    val filesContent=fileProcessor.processPath(path)
    filesContent match {
      case Failure(e) => Failure(e)
      case Success(filesContent) => {
        if (filesContent.isEmpty) {
          Failure(new Exception(s"Unable to index the folder ${path.getPath}"))
        } else {
          filesContent.foreach(file=>indexWordsFromFile(file._1,file._2))
          Success(filesContent.size)
        }
      }
    }
  }

  protected def indexWordsFromFile(fileName: String,content: String): Unit = {
    val wordCounts = content.split("\\s+").groupBy(identity).mapValues(_.length)
    val emptyTrie = TrieDict.emptyNode
    val trieDict = wordCounts.foldLeft(emptyTrie) { (trie, word) => trie.add(word._1, word._2) }
    dictionaries += fileName -> trieDict
  }

  def lookup(searchInput: String): mutable.ListMap[String, Int] = {
    val searchInputArray = searchInput.split("\\s+")
    val foundInFileNames = searchInputArray.flatMap(word => dictionaries.map(dic => (dic._1, dic._2.find(word)))
      .filter(_._2.isDefined).keys)
    val numOfMatchesByFileName = foundInFileNames.groupBy(identity).mapValues(_.length)
    val numOfMatchesByFileNamePer = numOfMatchesByFileName.map(coincidence =>
      (coincidence._1, coincidence._2 * 100 / searchInputArray.length))
    val sortedNumOfMatchesByFileName = numOfMatchesByFileNamePer.toSeq.sortWith(_._2 > _._2)
    mutable.ListMap(sortedNumOfMatchesByFileName: _*).take(numTopN)
  }
}