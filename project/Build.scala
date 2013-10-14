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
    cache,
    "securesocial" %% "securesocial" % "master-SNAPSHOT"
  )
  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
  )

}
