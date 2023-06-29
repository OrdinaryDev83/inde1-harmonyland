# inde1-harmonyland

## Authors
 - Tom Genlis - tom.genlis
 - Antoine Boutin - antoine.boutin
 - Bastien Pouessel - bastien.pouessel

Harmonyland, an extraordinary project developed at EPITA as part of our INDE2 program, showcases our expertise in data engineering.

---

## Versions
 - Scala 2.12.10
 - sbt 1.8.2
 - Open JDK 1.8.0_302
 - Spark 2.4.8

---

## How to build the project from scratch

In order to operate seamlessly, the Harmonyland project requires the utilization of OpenJDK 1.8 and Scala 2.12.10.
The Harmonyland project also relies on a Kafka broker and a Kafka ZooKeeper.

The project is composed of 5 principal components:
- drone_producer: simulate drone data that are written in a streams (Kafka)
- kafka_stream: use kafka stream to keep only alert
- discord_bot: consume the alert topic and send discord alert (Scala only)
- spark_streaming: consumer the topic and write it by micro-batch in cassandra database
- batch_processing: perform spark batch processing on the collected data


In order to build the different part of the project. It's possible to go in each sbt project and use the following command:
```
sbt clean assembly
```

Than you can run the jar with:
```
java -jar target/scala-2.12/<jar name>.jar
```

or just use `sbt run`

The project can be divided in tree part:
- *kafka_stream* and *discord_bot*
- *spark_streaming*
- *batch_processing*

They can be tested independently but the first parts require **drone_producer**.

To run the Proof of concept a kafka service needs to be up at localhost:9092.

The Cassandra database and grafana can be deployed with

```
docker-compose up -d // or podman-compose up -d
```

The cassandra script is available in order to configure all the tables `cqlsh < schema.cql`

Grafana will be up at `localhost:3000`

---

## Harmony Poc
The goal is to write a poc demonstrating a working architecture of harmonystate.
The typical architecture will have 4 components (probably 4 Main) :
  1. a program simulating the drone and sending drone-like data to your solution (see subject for
details on a message). This program should not be distributed (no spark).
Your system will store message in a distributed stream making it available to the component
2 and 3. (this part should not be done with spark)
  2. handle riot alert message from stream
 3. store message formatted as drone message in a distributed storage (ex: HDFS/S3)
 4. analyse stored data with a distributed processing component (like spark). As a proof of your
system capacity to analyse the store data, answer 4 questions of your choice. (ex: is there more
riot during the week or during week-end?).

All components may run independently, they must be scalable and used in a scalable way.
For component 3) you may use kafka connect or its equivalent (kinesis firehose).
Coding instruction

Any code must be written in functional scala (compiling to jvm on javascript doesn’t matter).
Unless I accept it as an exception the keywords «for, while, return, var, throw, null» are forbidden
as well as importing anything mutable. The method «.get» of the class Option is forbidden as
well.

Foreach as a collection or rdd method is accepted.

One exception for now : if you want to display a number of received/stored… messages or alerts
you may use the keyword «var».

Some student may choose as an option to code the five component in another functional
language (F#, Haskell…).

If you want to use nodejs for one of the five component you may use it through scala-js example
(https://github.com/scalajs-io/nodejs)
Spark code can either been in dataframe or rdd but don’t mix it in the same pipeline.

## Submission (2 points)
For the project you should use a git repo, work of different members of the group should be
visible in different commits.
For submission, you should email me with your git repo and the last commit hash.
Your repository must be private and you should grant me access.
Late submission emails are accepted, minus 2 points per late day(s).
Once those 4 parts done you can work on the personal part :
The personal part is quite open, the goal is for every group to work on something they are
curious about or they find interesting for their CV. They can be done in the language of your
choice unless you are using spark.
Here are some suggestion :
 0. running component 3 and 4 scaled on several component each.
 1. project deployed on the cloud (azure, aws, gcp,…) using IaC like terraform.
 2. website using its dedicated db/queue to display every received riot alert (instead of a
basic email/log/console print)
 3. using docker and docker compose for the 4 components of the project (and
kubernetes/mesos as ressource manager for spark if spark is used).
 4. once component 3 is done, using spark-notebook/zeppelin to generate charts
 5. using some dataviz or custom website to present the result of the spark analysis (within
an end to end pipeline)
 6. using an ml model and adding information in the message to achieve predictive
maintenance de of the drone.
 7. Whole project code in haskell/F#…
 8. any idea you find relevant for the project if I validate it

## FAQ
 - Except when doing the cloud option everything can be written and deploy locally on your
own computer (not distributed)
 - What matters is to create a working and scalable poc to demonstrate the architecture.
Analysis pertinence doesn’t matter.
 - Scoring of the citizen is already handled in the drone. You don't have to write any code to
adjust it apart from your drone simulator.
 - Most students should focus on the basic components. A group can achieve a good mark
(up to 16/20) without the personal part. The personal part is for the curious group which
want to do more.
 - If you want the cloud option to be 100% correct all the components apart from the drone
simulator should be running on the cloud.
 - the presentation should be done with slides for the context and the architecture, and a
small demo. Given time for presentation + demo is 10/12 minutes (without question

Authors : Antoine Boutin - Bastien Pouëssel - Tom Genlis
