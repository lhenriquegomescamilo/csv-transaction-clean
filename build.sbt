ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "csv-handler"
  )



resolvers += "Akka library repository".at("https://repo.akka.io/maven")

val AkkaVersion = "2.9.4"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "8.0.0",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % "test",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test"
)