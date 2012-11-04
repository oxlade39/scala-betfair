seq(githubPagesMavenPublishSettings: _*)

name := "scala-betfair"

organization := "com.github.oxlade39"

version := "1.1"

crossScalaVersions := Seq("2.9.1", "2.9.2")

// Java then Scala for main sources
compileOrder in Compile := CompileOrder.JavaThenScala

githubPagesCheckoutDir := Path.userHome / "proj" / "oxlade39.github.com" / "_site" / "maven"

publishMavenStyle := true

resolvers += "oxlade39.github.com" at "http://oxlade39.github.com/maven/"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.1",
  "org.joda" % "joda-convert" % "1.2",
  "org.joda" % "joda-convert" % "1.2",
  "com.github.oxlade39" % "betfair-ws" % "1.5",
  "org.pegdown" % "pegdown" % "1.0.2" % "test",
  "org.specs2" %% "specs2" % "1.9" % "test",
  "org.mockito" % "mockito-all" % "1.9.0" % "test"
)

testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "console", "html")