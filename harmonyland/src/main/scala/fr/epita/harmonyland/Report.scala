package fr.epita.harmonyland

import java.util.Date

case class Report(Id: String,
                  longitude: Double,
                  latitude: Double,
                  around: Map[String, Int],
                  words: List[String],
                  time: Date) {
}
