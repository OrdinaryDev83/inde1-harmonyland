package fr.epita.harmonyland

import java.util.Date
import scala.io.Source
import scala.math.Fractional.Implicits.infixFractionalOps
import scala.math.Numeric.IntIsIntegral.mkNumericOps
import scala.util.Random

object Simulation {
  def generateReport(droneId: Int): Report = {
    val n = (scala.util.Random.nextInt(3) + 1)
    val personsAndWords = List.fill(n)(generatePersonAndWords())
    val persons = personsAndWords.map(_._1)
    val words = personsAndWords.map(_._2)
    val surrounding = generateSurrounding(words)
    Report(
      droneId,
      generateRandomLongitude(),
      generateRandomLatitude(),
      persons,
      surrounding,
      new Date()
    )
  }

  private def generateRandomLongitude(): Double = {
    scala.util.Random.nextDouble() * 180 - 90
  }

  private def generateRandomLatitude(): Double = {
    scala.util.Random.nextDouble() * 360 - 180
  }

  private def generateWordsAndScore(): (List[(String, Int)], Int) = {
    // get a random number of words
    val n = scala.util.Random.nextInt(10) + 1
    val tuples = List.fill(n)(scala.util.Random.shuffle(this.words).head)
    // calculate average of the words
    val tuples2 = tuples.collect {
      case (a, str) if str.matches("-?\\d+(\\.\\d+)?") => (a, str.toInt)
    }
    val numbers = tuples2.map(_._2).map(x => x.toDouble)
    (tuples2, (numbers.sum / numbers.length).toInt)
  }

  private def generateSurrounding(words : List[List[(String, Int)]]): List[String] = {
    // concat all the words said by each person and sort it by score
    words.flatten.sortBy(_._2).map(_._1)
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
  private val words = read_csv("data/words.csv")

  private def generatePersonAndWords(): (Person, List[(String, Int)]) = {
    val personName = Random.shuffle(names).head
    val tuple = generateWordsAndScore()
    (Person(personName._1, personName._2, tuple._2), tuple._1)
  }

  case class Report(droneId: Int, longitude: Double, latitude: Double, persons: List[Person], words: List[String], time: Date) extends Product with Serializable

  case class Person(firstname: String, lastname: String, harmonyscore: Int) extends Product with Serializable{
    override def toString: String = {
      s"$firstname $lastname: $harmonyscore"
    }
  }
}
