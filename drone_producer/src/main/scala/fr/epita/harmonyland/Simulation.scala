package fr.epita.harmonyland

import java.sql.Timestamp
import java.util.Date
import scala.io.Source
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
      Timestamp.from(new Date().toInstant)
    )
  }

  private def generateRandomLongitude(): Double = {
    scala.util.Random.nextDouble() * 180 - 90
  }

  private def generateRandomLatitude(): Double = {
    scala.util.Random.nextDouble() * 360 - 180
  }

  private def getARandomWord(harmonyScore : Int) : (String, Int) = {
    val words_converted = this.words.collect {
      case (a, str) if str.matches("-?\\d+(\\.\\d+)?") => (a, str.toInt)
    }
    val words = words_converted.filter(_._2 == harmonyScore)
    words(Random.nextInt(words.length))
  }

  def randomHarmonyScore(): Int = {
    val rand = new Random
    val mean = 5.0
    val stdDev = 4.68
    val gaussian = rand.nextGaussian() * stdDev + mean // generates a value from normal distribution centered around 5
    val scaled = (gaussian - (-2.5)) / (12.5 - (-2.5)) * 10 // scales it between 0 and 10
    Math.min(Math.max(scaled, 0), 10).floor.toInt // clips to 0 or 10 if it falls outside the range
  }

  private def generateWordsAndScore(): (List[(String, Int)], Int) = {
    // get a random number of words
    val n = scala.util.Random.nextInt(10) + 1
    val tuples = List.fill(n)(getARandomWord(randomHarmonyScore()))
    // calculate average of the words
    val numbers = tuples.map(_._2).map(x => x.toDouble)
    (tuples, (numbers.sum / numbers.length).toInt)
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

  case class Report(droneId: Int, longitude: Double, latitude: Double, persons: List[Person], words: List[String], time: Timestamp) extends Product with Serializable

  case class Person(firstname: String, lastname: String, harmonyscore: Int) extends Product with Serializable{
    override def toString: String = {
      s"$firstname $lastname: $harmonyscore"
    }
  }
}
