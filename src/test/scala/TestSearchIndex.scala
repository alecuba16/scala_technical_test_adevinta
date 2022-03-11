package org.alecuba16

import org.mockito.Mockito._
import org.scalatest.FunSpec
import org.scalatest.mockito.MockitoSugar

import java.io.File
import scala.util.{Failure, Success}

class TestSearchIndex extends FunSpec with MockitoSugar {
  val NumTopN = 10


  describe("A SearchIndex") {
    it("the file indexer should return the number of files indexed") {
      val mockFileProcessor = mock[FileProcessor]
      val failurePath = new File("/null/test")
      val msg = s"The ${failurePath.getPath} doesn't exists"
      when(mockFileProcessor.processPath(failurePath)) thenReturn (Failure(new Exception(msg)))
      val index = new SearchIndex(NumTopN, mockFileProcessor)
      val thrown = intercept[Exception] {
        index.indexFiles(failurePath).get
      }
      assert(thrown.getMessage === msg)
    }

    it("should return a failure if a folder is empty") {
      val mockFileProcessor = mock[FileProcessor]
      val emptyPath = new File("/empty/folder")
      val msg = s"The ${emptyPath.getPath} doesn't contains any file"
      when(mockFileProcessor.processPath(emptyPath)) thenReturn (Failure(new Exception(msg)))
      val index = new SearchIndex(NumTopN, mockFileProcessor)
      val thrown = intercept[Exception] {
        index.indexFiles(emptyPath).get
      }
      assert(thrown.getMessage === msg)
    }

    it("should return the number of files in a directory") {
      val mockFileProcessor = mock[FileProcessor]
      val folderWithFiles = new File("/filder/with/files/")
      val response =  List("file1", "file2", "file3").map(file => (s"${folderWithFiles.getPath}/$file",s"This is the content of the file $file")).toMap
      when(mockFileProcessor.processPath(folderWithFiles)) thenReturn (Success(response))
      val index = new SearchIndex(NumTopN, mockFileProcessor)
      val numFilesIndexed =index.indexFiles(folderWithFiles)
      assert(numFilesIndexed.get == response.size)
    }

    it("should find the text in all the files") {
      val mockFileProcessor = mock[FileProcessor]
      val folderWithFiles = new File("/filder/with/files/")
      val textToFind = "the content"
      val response =  List("file1", "file2", "file3").map(file => (s"${folderWithFiles.getPath}/$file",s"This is the content of the file $file")).toMap
      when(mockFileProcessor.processPath(folderWithFiles)) thenReturn (Success(response))
      val index = new SearchIndex(NumTopN, mockFileProcessor)
      index.indexFiles(folderWithFiles)
      val coincidences=index.lookup(textToFind)
      assert(coincidences.size == response.size)
    }

    it("should indicate that an error because it wasn't able to process the files") {
      val mockFileProcessor = mock[FileProcessor]
      val folderWithFiles = new File("/filder/with/files/")
      when(mockFileProcessor.processPath(folderWithFiles)) thenReturn (Success(Map[String,String]()))
      val msg = s"Unable to index the folder ${folderWithFiles.getPath}"
      val index = new SearchIndex(NumTopN, mockFileProcessor)
      val thrown = intercept[Exception] {
        index.indexFiles(folderWithFiles).get
      }
      assert(thrown.getMessage === msg)
    }

    it("should indicate a coincidence of 100% in all the files") {
      val mockFileProcessor = mock[FileProcessor]
      val folderWithFiles = new File("/filder/with/files/")
      val textToFind = "the content"
      val response =  List("file1", "file2", "file3").map(file => (s"${folderWithFiles.getPath}/$file",s"This is the content of the file $file")).toMap
      when(mockFileProcessor.processPath(folderWithFiles)) thenReturn (Success(response))
      val index = new SearchIndex(NumTopN, mockFileProcessor)
      index.indexFiles(folderWithFiles)
      val coincidences=index.lookup(textToFind)
      assert(coincidences.nonEmpty & coincidences.forall(_._2==100))
    }


  }
}
