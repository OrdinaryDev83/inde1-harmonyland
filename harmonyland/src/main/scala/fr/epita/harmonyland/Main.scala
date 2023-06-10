package fr.epita.harmonyland

import com.google.gson.GsonBuilder

import java.util.concurrent.{Executors, TimeUnit}

object Main extends App {
  private val gson = new GsonBuilder().setPrettyPrinting().create()
  private val scheduler = Executors.newScheduledThreadPool(1)

  scheduler.scheduleAtFixedRate(
    () => {
      List.range(0, 10)
        .map(Simulation.generateReport)
        .map(gson.toJson)
        .foreach(println)
    },
    0,
    1,
    TimeUnit.MINUTES
  )
}
