organization in ThisBuild := "org.scala-lang.modules"

name := "scalac-jigsaw"

description in ThisBuild := "Middle-man to avoid direct access to java.lang.module._ from scalac"

version in ThisBuild := "0.1-SNAPSHOT"

homepage := Some(url("http://github.com/retronym/scalac-jigsaw"))

startYear := Some(2016)

licenses +=("Scala license", url("https://github.com/retronym/scalac-jigsaw/blob/master/LICENSE"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <developers>
    <developer>
      <id>retronym</id>
      <name>Jason Zaugg</name>
      <timezone>+10</timezone>
      <url>http://github.com/retronym</url>
    </developer>
  </developers>
    <scm>
      <url>git@github.com:retronym/scalac-jigsaw.git/</url>
      <connection>scm:git:git@github.com:retronym/scalac-jigsaw.git</connection>
    </scm>
  )

//osgiSettings
//
//val osgiVersion = version(_.replace('-', '.'))
//
//OsgiKeys.bundleSymbolicName := s"${organization.value}.${name.value}"


// maven publishing
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  val repo = if (version.value.trim.endsWith("SNAPSHOT"))
    "snapshots" at nexus + "content/repositories/snapshots"
  else
    "releases" at nexus + "service/local/staging/deploy/maven2"
  Some(repo)
}

lazy val root = (project in file(".")).
  aggregate(scalacJigsawInterface).
  settings(inThisBuild(List(
      organization := "org.scala-lang.modules",
      autoScalaLibrary := false
    )),
    name := "scalac-jigsaw"
  )

lazy val scalacJigsawInterface = (project in file("scalac-jigsaw-interface")).
  settings(
    name := "scalac-jigsaw-interface"
  )

lazy val scalacJigsawJava9 = (project in file("scalac-jigsaw-java9")).
  settings(
    name := "scalac-jigsaw-java9",
    libraryDependencies += "junit" % "junit-dep" % "4.10" % "test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test",
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v", "-s")
  ).
  dependsOn(scalacJigsawInterface)
