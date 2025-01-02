lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "airports-backend",
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      guice,
      "org.playframework.anorm" %% "anorm" % "2.8.1",
    ),
  )

scalaVersion := "2.13.15"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.9.0"
libraryDependencies += "com.typesafe" % "config" % "1.4.2"


