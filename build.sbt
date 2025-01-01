lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

name := "AirportProject"

version := "0.1"

scalaVersion := "2.13.15"

libraryDependencies ++= Seq(
  guice,
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.9.0",
  "com.typesafe" % "config" % "1.4.2",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC5"
)

javacOptions ++= Seq("--release", "8")
