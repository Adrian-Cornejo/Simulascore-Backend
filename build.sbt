name := """Simulascore-Backend"""
organization := "adrian.com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.15"

val playVersion = "3.0.6"  // Actualizamos a la última versión estable de Play 3.x

libraryDependencies ++= Seq(
  guice,
  "org.playframework" %% "play" % playVersion,
  "org.playframework" %% "play-slick" % "6.0.0",  // Compatible con Play 3.x
  "org.playframework" %% "play-slick-evolutions" % "6.0.0",
  "mysql" % "mysql-connector-java" % "8.0.33",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
  "org.playframework" %% "play-json" % "3.0.4",  // Versión compatible con Play 3.x
  "com.auth0" % "java-jwt" % "4.4.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.playframework" %% "play-mailer" % "10.0.0",  // Última versión compatible con Play 3.x
  "org.playframework" %% "play-mailer-guice" % "10.0.0"
)

// Forzar versiones específicas para resolver conflictos
dependencyOverrides ++= Seq(
  "org.scala-lang" % "scala-library" % "2.13.15",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.3",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.14.3",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.14.3",
  "org.slf4j" % "slf4j-api" % "2.0.16"
)

// Excluir Akka ya que estamos usando Pekko
libraryDependencies := libraryDependencies.value.map(_.exclude("com.typesafe.akka", "akka-actor_2.13"))

// Permitir resolver conflictos menores en dependencias
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "early-semver"