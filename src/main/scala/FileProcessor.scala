package org.alecuba16

import scala.util.{Failure, Success, Try}
import java.io.File
import scala.io.Source

class FileProcessor {
  def processPath(files: File): Try[Map[String, String]] = {
    val availableElements = files.listFiles
    availableElements match {
      case null => Failure( new Exception(s"The ${files.getPath} doesn't exists"))
      case _ => {
        val fileList = availableElements.filter(_.isFile)
        fileList.length match {
          case 0 => Failure(new Exception(s"The ${files.getPath} doesn't contains any file"))
          case _ =>Success(fileList.map(fileName => (fileName.getPath, getFileContent(fileName))).toMap)
        }
      }
    }
  }

  def getFileContent(fileName: File): String = {
    val source = Source.fromFile(fileName)
    val lines = try source.getLines().mkString(" ").toLowerCase.filter(content => content.toString.matches("[a-z 0-9]"))  finally source.close()
    lines
  }


}
