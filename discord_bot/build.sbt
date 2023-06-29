name := "Discord Bot Alert Consumer"

version := "1.0"

scalaVersion := "2.12.10"

resolvers += Resolver.JCenterRepository

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "io.github.cdimascio" % "java-dotenv" % "5.2.0",
  "org.apache.kafka" % "kafka-clients" % "3.5.0",
  "org.apache.kafka" % "kafka-clients" % "3.2.0",
  "net.katsstuff" %% "ackcord" % "0.18.1", //For high level API, includes all the other modules
  "net.katsstuff" %% "ackcord-core" % "0.18.1", //Low level core API
  "net.katsstuff" %% "ackcord-commands" % "0.18.1", //Low to mid level Commands API
  "net.katsstuff" %% "ackcord-lavaplayer-core" % "0.18.1", //Low level lavaplayer API
  "org.apache.kafka" % "kafka-clients" % "2.8.0"
)

resolvers += "LavaPlayerRepo" at "https://m2.dv8tion.net/releases"
libraryDependencies += "com.sedmelluq" % "lavaplayer" % "1.3.77"

assembly / assemblyJarName := "discord-bot-fatjar-1.0.jar"

assembly / assemblyMergeStrategy := {
 case PathList("META-INF", _*) => MergeStrategy.discard
 case _                        => MergeStrategy.first
}
