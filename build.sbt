import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtPgp.PgpKeys.publishSigned
import xerial.sbt.Sonatype.sonatypeSettings


val `version.java`        = "1.8"
val `version.scala`       = "2.11.7"
val `version.osgi`        = "4.3.0"
val `version.domino`      = "1.1.0"
val `version.slf4j`       = "1.7.7"
val `version.logback`     = "1.1.3"
val `version.logging`     = "3.1.0"
val `version.pax-url`     = "2.4.1"
val `version.pax-logging` = "1.8.3"

val `version.utest`       = "0.3.1"
val `version.minitest`    = "0.12"
val `version.little-spec` = "0.4"
val `version.otest`       = "0.2.1"


// compilation options -------------------------------------------------------------------------------------------------

val javacOpts : Seq[String] =
  Seq(
    "-encoding", "UTF-8",
    "-source", `version.java`
  )

val scalacOpts : Seq[String] =
  Seq(
    "-unchecked", "-deprecation", "-feature"
  )

val javacCheckOpts: Seq[String] =
  Seq(
    "-Xlint"
  )

val scalacCheckOpts: Seq[String] =
  Seq(
    "-Xlint",
    "-Yinline-warnings", "-Ywarn-adapted-args",
    "-Ywarn-dead-code", "-Ywarn-inaccessible", "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit", "-Xlog-free-terms"
  )

val librarySettings : Seq[Setting[_]] =
  Seq(
    organization       := "com.github.frgomes.domino",

    scalaVersion       := `version.scala`,
    //XXX crossPaths         := true,
    //XXX crossScalaVersions := Seq("2.11.7"),

    compileOrder in Compile := CompileOrder.JavaThenScala,
    compileOrder in Test    := CompileOrder.JavaThenScala,

    javacOptions  ++= javacOpts,
    scalacOptions ++= scalacOpts,
    fork          :=  true
  )

val paranoidOptions : Seq[Setting[_]] =
  Seq(
    javacOptions  in (Compile, compile) ++= javacCheckOpts,
    javacOptions  in (Test, compile)    ++= javacCheckOpts,
    scalacOptions ++= scalacCheckOpts
  )

val optimizationOptions : Seq[Setting[_]] =
  Seq(
    scalacOptions ++= Seq("-optimise")
  )

val documentationOptions : Seq[Setting[_]] =
  Seq(
    javacOptions  in (Compile,doc) ++= Seq("-Xdoclint", "-notimestamp", "-linksource"),
    scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits")
  )


// code generation -----------------------------------------------------------------------------------------------------

val managedSources : Seq[Setting[_]] =
  Seq(
    managedSourceDirectories in Compile +=
      baseDirectory.value / "target" / scalav(scalaVersion.value) / "src_managed" / "main" / "java",
    managedSourceDirectories in Test +=
      baseDirectory.value / "target" / scalav(scalaVersion.value) / "src_managed" / "test" / "java"
  )


// dependencies --------------------------------------------------------------------------------------------------------

lazy val deps_osgi: Seq[Setting[_]] =
  Seq(
    resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
    libraryDependencies ++= Seq(
      "org.osgi"               %  "org.osgi.core"                         % `version.osgi`, 
      "org.osgi"               %  "org.osgi.compendium"                   % `version.osgi`, 
      "com.github.domino-osgi" %% "domino"                                % `version.domino`,
      "org.ops4j.pax.logging"  %  "pax-logging-api"                       % `version.pax-logging`

      //TODO These libraries will be tested in future
      //XXX "org.ops4j.pax.url"      %  "pax-url-classpath"                     % `version.pax-url`,
      //XXX "org.ops4j.pax.url"      %  "pax-url-obr"                           % `version.pax-url`,
      //XXX "org.ops4j.pax.url"      %  "pax-url-assembly"                      % `version.pax-url`
      //XXX "org.ops4j.pax.url"      %  "pax-url-mvn"                           % "1.3.7",
      //XXX "org.apache.felix"       %  "org.apache.felix.framework"            % `version.osgi`  % "runtime",
      //XXX "org.apache.felix"       %  "org.apache.felix.fileinstall"          % "3.5.0"         % "provided",
      //XXX "org.apache.felix"       %  "org.apache.felix.scr"                  % "1.8.2"         % "provided",
      //XXX "org.apache.felix"       %  "org.apache.felix.bundlerepository"     % "2.0.4",
    ))

val deps_essential : Seq[Setting[_]] =
  Seq(
    libraryDependencies ++= Seq(
      "org.slf4j"                  %  "slf4j-api"                         % `version.slf4j`,
      "org.slf4j"                  %  "log4j-over-slf4j"                  % `version.slf4j`,
      "ch.qos.logback"             %  "logback-classic"                   % `version.logback`,
      "com.typesafe.scala-logging" %% "scala-logging"                     % `version.logging`
    ))



// utility functions --------------------------------------------------------------------------------------------

val debug = false


def scalav(scalaVersion: String) =
  if (scalaVersion startsWith "2.10.") "scala-2.10" else "scala-2.11"


// Generic launcher for external applications.
// Ex: runner("/bin/ls", Seq("-al"), Option("/etc"), HashMap.empty[String,String])
def runner(app: String,
           args: Seq[String],
           cwd: Option[File] = None,
           env: Map[String, String] = Map.empty): Int = {
  import scala.collection.JavaConverters._

  val cmd: Seq[String] = app +: args
  val pb = new java.lang.ProcessBuilder(cmd.asJava)
  if (cwd.isDefined) pb.directory(cwd.get)
  pb.inheritIO
  //FIXME: set environment
  val process = pb.start()
  def cancel() = {
    //XXX println("Run canceled.")
    process.destroy()
    15
  }
  try process.waitFor catch { case e: InterruptedException => cancel() }
}


// Generic Java launcher
def javaRunner(classpath: Option[Seq[File]] = None,
               runJVMOptions: Seq[String] = Nil,
               mainClass: Option[String] = None,
               args: Seq[String],
               cwd: Option[File] = None,
               javaHome: Option[File] = None,
               javaTool: Option[String] = None,
               envVars: Map[String, String] = Map.empty,
               connectInput: Boolean = false,
               outputStrategy: Option[OutputStrategy] = Some(StdoutOutput)): Unit = {

  val app : String      = javaHome.fold("") { p => p.absolutePath + "/bin/" } + javaTool.getOrElse("java")
  val jvm : Seq[String] = runJVMOptions.map(p => p.toString)
  val cp  : Seq[String] =
    classpath
      .fold(Seq.empty[String]) { paths =>
      Seq(
        "-cp",
        paths
          .map(p => p.absolutePath)
          .mkString(java.io.File.pathSeparator))
    }
  val klass = mainClass.fold(Seq.empty[String]) { name => Seq(name) }
  val xargs : Seq[String] = jvm ++ cp ++ klass ++ args

  if(debug) {
    println("=============================================================")
    println(s"${app} " + xargs.mkString(" "))
    println("=============================================================")
    println("")
  }

  if (cwd.isDefined) IO.createDirectory(cwd.get)
  val errno = runner(app, xargs, cwd, envVars)
  if(errno!=0) throw new IllegalStateException(s"errno = ${errno}")
}


// projects -----------------------------------------------------------------------------------------------------

lazy val root =
  project.in(file("."))
    .settings(buildInfoSettings:_*)
    .settings(disablePublishing:_*)
    .aggregate(moduleA, moduleB)

lazy val moduleA =
  project.in(file("moduleA"))
    .enablePlugins(SbtOsgi)
    .settings(disablePublishing:_*)
    .settings(librarySettings:_*)
    .settings(paranoidOptions:_*)
    .settings(deps_osgi:_*)
    .settings(deps_essential:_*)
    .settings(
      Seq(
        OsgiKeys.exportPackage  := Seq("moduleA.api"),
        OsgiKeys.privatePackage := Seq("moduleA.internal"),
        OsgiKeys.importPackage :=
          Seq(
            "sun.misc;resolution:=optional",
            "!aQute.bnd.annotation.*",
            "*" ),
        OsgiKeys.bundleActivator := Option("moduleA.internal.MyActivator"),
        OsgiKeys.additionalHeaders :=
          Map(
            "Service-Component" -> "*",
            "Conditional-Package" -> "scala.*" )
      ):_*)

lazy val moduleB =
  project.in(file("moduleB"))
    .enablePlugins(SbtOsgi)
    .settings(disablePublishing:_*)
    .settings(librarySettings:_*)
    .settings(paranoidOptions:_*)
    .settings(deps_osgi:_*)
    .settings(deps_essential:_*)
    .settings(
      Seq(
        OsgiKeys.exportPackage  := Seq("moduleB.api"),
        OsgiKeys.privatePackage := Seq("moduleB.internal"),
        OsgiKeys.importPackage :=
          Seq(
            "sun.misc;resolution:=optional",
            "!aQute.bnd.annotation.*",
            "*" ),
        OsgiKeys.bundleActivator := Option("moduleB.internal.MyDominoActivator"),
        OsgiKeys.additionalHeaders :=
          Map(
            "Service-Component" -> "*",
            "Conditional-Package" -> "scala.*" )
      ):_*)




// publish settings ---------------------------------------------------------------------------------------------

lazy val disablePublishing =
  sonatypeSettings ++
    Seq(
      publishArtifact := false,
      publishSigned := (),
      publish := (),
      publishLocal := ()
    )


//NOTE: These publishingSettings below DO NOT WORK.
//NOTE: And are provided only as an example on how publishing can be configured.
lazy val publishSettings =
  sonatypeSettings ++
    Seq(
      publishTo := {
        val nexus = "https://bintray.com/frgomes/maven-repository"
        if (isSnapshot.value)
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      },
      pomIncludeRepository := { _ => false },
      pomExtra := {
        <url>http://github.com/frgomes/domino-osgi-sample</url>
          <licenses>
            <license>
                <name>BSD</name>
                <url>http://opensource.org/licenses/BSD-3-Clause</url>
            </license>
          </licenses>
          <scm>
            <connection>scm:git:github.com/frgomes/domino-osgi-sample.hg</connection>
            <developerConnection>scm:git:frgomes@github.com:frgomes/domino-osgi-sample.hg</developerConnection>
            <url>http://github.com/frgomes/domino-osgi-sample</url>
          </scm>
          <developers>
            <developer>
              <id>frgomes</id>
              <name>Richard Gomes</name>
              <url>http://rgomes-info.blogspot.com</url>
            </developer>
          </developers>
      }
    )
