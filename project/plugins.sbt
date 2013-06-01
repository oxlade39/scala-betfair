resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.0-SNAPSHOT")

resolvers += "oxlade39.github.com" at "http://oxlade39.github.com/maven/"

addSbtPlugin("com.github.oxlade39" % "github-maven-publish-plugin" % "1.0")