name := """Simulascore-Backend"""
organization := "adrian.com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  guice, // Inyección de dependencias

  "com.typesafe.play" %% "play-slick" % "5.0.0", // Slick para manejo de BD
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0", // Para migraciones en BD
  "mysql" % "mysql-connector-java" % "8.0.33", // Conector JDBC para MySQL
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test, // Testing en Play
  "com.typesafe.play" %% "play-json" % "2.9.4",// JSON parsing
  "com.auth0" % "java-jwt" % "4.4.0",
  "org.mindrot" % "jbcrypt" % "0.4"
)

// Resolver el conflicto de versiones en scala-xml
dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "2.2.0"

// Permitir resolver conflictos menores en dependencias
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "early-semver"