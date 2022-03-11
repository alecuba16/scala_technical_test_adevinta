package org.alecuba16

import org.alecuba16.Utils.printFormattedOutput

import java.io.File
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      throw new IllegalArgumentException("No directory given to index.")
    }

    val path = args(0)

    // Generate the search index
    val NumTopN = 10
    val fileProcessor = new FileProcessor
    val index = new SearchIndex(NumTopN,fileProcessor)
    val files = new File(path)
    val validIndex = index.indexFiles(files)

    validIndex match {
      case Failure(ex) => ex
      case  Success(numberOfFiles)=> println(s"${numberOfFiles} files read in directory $path")
    }

    // Wait for the user's input
    while (true) {
      val userInput = scala.io.StdIn.readLine("search> ")
      if (userInput == ":quit") System.exit(0)
      if (!userInput.matches("[a-zA-Z0-9 ]+")) {
        println("Invalid search input, valid inputs: [a-zA-Z ]")
      } else {
        //val selectedSortedList = index.lookup(userInput.toLowerCase)
        val selectedSortedList = index.lookup(userInput.toLowerCase)

        printFormattedOutput(selectedSortedList)
      }
    }
  }

}
