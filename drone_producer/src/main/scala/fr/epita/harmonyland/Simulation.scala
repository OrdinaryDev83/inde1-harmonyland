package fr.epita.harmonyland

import java.util.Date
import scala.io.Source
import scala.util.Random

object Simulation {
  def generateReport(droneId: Int): Report = {
    Report(
      droneId,
      generateRandomLongitude(),
      generateRandomLatitude(),
      generateSurrounding(),
      generateWords(),
      new Date()
    )
  }

  private def generateRandomLongitude(): Double = {
    scala.util.Random.nextDouble() * 180 - 90
  }

  private def generateRandomLatitude(): Double = {
    scala.util.Random.nextDouble() * 360 - 180
  }

  private def generateWords(): List[String] = {
    List.fill(scala.util.Random.nextInt(10))(scala.util.Random.nextPrintableChar().toString)
  }

  private def generateSurrounding(): List[Person] = {
    List.fill(scala.util.Random.nextInt(10))(generatePerson())
  }

  private def read_csv(filePath : String) : List[(String, String)] = {
    val bufferedSource = Source.fromFile(filePath)
    bufferedSource match {
      case null => throw new Exception("File not found")
      case _ => bufferedSource
        .getLines()
        .map(_.split(","))
        .map(x => (x.head, x.tail.head))
        .toList
    }
  }

  private val names = read_csv("data/names.csv")

  private def generatePerson(): Person = {
    val personName = Random.shuffle(names).head
    Person(personName._1, personName._2, generateScore())
  }

  private def generateScore(): Int = {
    scala.util.Random.nextInt(11)
  }

  case class Report(droneId: Int, longitude: Double, latitude: Double, persons: List[Person], words: List[String], time: Date)

  case class Person(firstName: String, lastName: String, harmonyScore: Int)
}
