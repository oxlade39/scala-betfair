GithubPagesMavenPublish.githubPagesMavenPublishSettings

name := "scala-betfair"

organization := "com.github.oxlade39"

version := "2.0-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

// crossScalaVersions := Seq("2.10.4", "2.11.2")

githubPagesCheckoutDir := Path.userHome / "proj" / "oxlade39.github.com" / "maven"

publishMavenStyle := true

libraryDependencies ++= {
  val playVersion = "2.3.6"
  Seq(
    "com.typesafe.play" %% "play-ws" % playVersion,
    "org.mockito" % "mockito-all" % "1.9.0" % "test"
  )}

libraryDependencies <+= scalaVersion {
  case "2.10.4" => "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"
  case _ => "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
}
