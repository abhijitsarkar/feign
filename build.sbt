import sbt.Keys._

name := """feign"""

val slf4jVersion = "1.7.21"
val logbackVersion = "1.1.7"
val specs2Version = "3.7"

lazy val commonSettings = Seq(
  organization := "org.abhijitsarkar.feign",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "org.specs2" %% "specs2-core" % specs2Version % Test,
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "ch.qos.logback" % "logback-core" % logbackVersion % Runtime,
    "ch.qos.logback" % "logback-classic" % logbackVersion % Runtime
  ),
  scalacOptions := Seq(
    "-feature", "-unchecked", "-deprecation", "-encoding", "utf8"
  )
)

lazy val `feign-api` = project
  .settings(commonSettings: _*)

val moultingyamlVersion = "0.3.1"
lazy val `feign-domain` = project
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq("net.jcazevedo" %% "moultingyaml" % moultingyamlVersion))
  .dependsOn(`feign-api`)

lazy val `feign-matcher` = project
  .settings(commonSettings: _*)
  .dependsOn(`feign-api`)

lazy val feign = project.in(file("."))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .aggregate(`feign-api`, `feign-domain`, `feign-matcher`)
  .dependsOn(`feign-api` % "test->test;compile->compile")
  .dependsOn(`feign-domain` % "test->test;compile->compile")
  .dependsOn(`feign-matcher` % "test->test;compile->compile")

val reactiveMongoVersion = "0.12.0"
val catsVersion = "0.8.1"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVersion,
  "org.typelevel" %% "cats" % catsVersion
)

fork in run := false