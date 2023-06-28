name := "Batch Processing"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.8",
  "org.apache.spark" %% "spark-sql" % "2.4.8",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.5.2",
  "com.github.jnr" % "jnr-ffi" % "2.1.10", // Replace with the version that is compatible with your setup
  "com.github.jnr" % "jnr-posix" % "3.0.50" // Replace with the version that is compatible with your setup
)

