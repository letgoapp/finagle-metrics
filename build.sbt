import Dependencies._
import sbt.Path
import ReleaseTransformations._

lazy val root = (project in file("."))
val appName   = "finagle-metrics"

name                      := appName
organization              := "com.github.rlazoti"
scalaVersion in ThisBuild := "2.12.1"
crossScalaVersions        := Seq("2.11.8", "2.12.1")
releaseTagName            := s"v${(version in ThisBuild).value}"
releaseTagComment         := s"[BUILD] Release ${(version in ThisBuild).value}"
releaseCommitMessage      := s"[BUILD] Set version to ${(version in ThisBuild).value}"

lazy val artifactoryHost = "internal-bipro-artifactory-1634906620.us-east-1.elb.amazonaws.com"
publishTo := Some(
  "spark-release-local" at s"http://$artifactoryHost/artifactory/spark-release-local")
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

scalacOptions := Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:postfixOps",
  "-target:jvm-1.8")

javacOptions in compile ++= Seq(
  "-target", "8",
  "-source", "8")

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true))

resolvers           ++= appDependencyResolvers
libraryDependencies ++= appDependencies
