package fr.epita.harmonyland

import ackcord.Requests
import ackcord.interactions._
import ackcord.interactions.commands._

class HelloWorldCommand(requests: Requests) extends CacheApplicationCommandController(requests) {

  val pongCommand = SlashCommand.command("ping", "Check if the bot is alive") { _ =>
    sendMessage("pong")
  }
}
