import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Notes"
  val appVersion      = "0.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "securesocial" %% "securesocial" % "2.1.1",
    "org.pegdown" % "pegdown" % "1.4.1",
    "com.amazonaws" % "aws-java-sdk" % "1.3.11"
  )
  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
  )

}

