package fr.epita.harmonyland

import java.util.Date

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

  private def generateWords(): Array[String] = {
    Array.fill(scala.util.Random.nextInt(10))(scala.util.Random.nextPrintableChar().toString)
  }

  private def generateSurrounding(): Array[Person] = {
    Array.fill(scala.util.Random.nextInt(10))(generatePerson())
  }

  private def generatePerson(): Person = {
    Person(
      generateName(),
      generateName(),
      generateScore()
    )
  }

  private def generateName(): String = {
    Array.fill(scala.util.Random.nextInt(10))(scala.util.Random.nextPrintableChar()).mkString
  }

  private def generateScore(): Int = {
    scala.util.Random.nextInt(11)
  }

  case class Report(droneId: Int, longitude: Double, latitude: Double, persons: Array[Person], words: Array[String], time: Date)

  case class Person(firstName: String, lastName: String, harmonyScore: Int)
}
