package org.alecuba16

import scala.collection.mutable

object Utils {
  def printFormattedOutput(selectedSortedList: mutable.ListMap[String, Int]): Unit = {
    if (selectedSortedList.isEmpty) {
      println("no matches found")
      return
    }
    selectedSortedList.foreach(listElem => {
      val fileName = listElem._1.split("/").last
      val percentage = listElem._2
      print(s"$fileName : $percentage%")
      println()
    })
  }
}
