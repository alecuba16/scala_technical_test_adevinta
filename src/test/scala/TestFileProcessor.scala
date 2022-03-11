package org.alecuba16
import org.mockito.Matchers.any
import org.scalatest.FunSpec

import java.io.File
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

import scala.io.{BufferedSource, Source}
import scala.util.Failure

class TestFileProcessor extends FunSpec with MockitoSugar {
  val NumTopN = 10
  val fileProcessor = new FileProcessor()

  describe("A FileIndexer") {
    it("should return a failure if a folder doesn't exists") {
      val mockFile = mock[File]
      val failurePath = "/null/test"
      when(mockFile.getPath) thenReturn failurePath
      val msg=s"The $failurePath doesn't exists"

      when(mockFile.listFiles()) thenReturn (null)
      val thrown = intercept[Exception] {
        fileProcessor.processPath(mockFile).get
      }
      assert(thrown.getMessage === msg)
    }

    it("should return a failure if a folder is empty") {
      val mockFile = mock[File]
      val emptyPath = "/empty/folder"
      when(mockFile.getPath) thenReturn emptyPath
      when(mockFile.listFiles) thenReturn (Array.empty[File])
      val msg=s"The $emptyPath doesn't contains any file"
      val thrown = intercept[Exception] {
        fileProcessor.processPath(mockFile).get
      }
      assert(thrown.getMessage === msg)
    }

    it("should return the number of files in a directory") {
      val pathWithFiles = "/folder/with/files/"
      val mockedFiles = List("file1", "file2", "file3").map(file => {
        val mockedFile = mock[File]
        when(mockedFile.isFile) thenReturn true
        when(mockedFile.getName) thenReturn file
        when(mockedFile.getPath) thenReturn s"$pathWithFiles$file"
        mockedFile
      })

      val mockedFolder = mock[File]
      when(mockedFolder.getPath) thenReturn pathWithFiles
      when(mockedFolder.listFiles) thenReturn mockedFiles.toArray[File]

      val mockedSource = spy(new FileProcessor)
      doReturn("test content").when(mockedSource).getFileContent(any[File])

      val numFiles = mockedSource.processPath(mockedFolder)
      assert(numFiles.get.size == mockedFiles.length)
    }
  }
}
