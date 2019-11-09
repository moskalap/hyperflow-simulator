
lazy val versions = new Object {
  val akka = "2.5.25"
  val akkaHttp = "10.1.10"
}

lazy val hyperflowSimulator = (project in file(".")).
  settings(
    organizationName := "pl.edu.agh",
    name := "hyperflow-simulator",
    version := "0.0.1-SNAPSHOT",
    mainClass in (Compile, run) := Some("pl.edu.agh.hyperflow.simulator.Simulator"),
    mainClass in (Compile, packageBin) := Some("pl.edu.agh.hyperflow.simulator.Simulator"),
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % "2.12.7",
      "org.scala-lang" % "scala-reflect" % "2.12.7",
      "org.scala-lang" % "scala-compiler" % "2.12.7",
      "com.typesafe.akka" %% "akka-actor" % versions.akka,
      "com.typesafe.akka" %% "akka-testkit" % versions.akka % Test,
      "com.typesafe.akka" %% "akka-stream" % versions.akka,
      "com.typesafe.akka" %% "akka-stream-testkit" % versions.akka % Test,
      "com.typesafe.akka" %% "akka-http" % versions.akkaHttp,
      "com.typesafe.akka" %% "akka-http-testkit" % versions.akkaHttp % Test,
      "com.typesafe.akka" %% "akka-http-spray-json" % versions.akkaHttp,
      "com.typesafe.akka" %% "akka-slf4j" % versions.akka,
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.cloudsimplus" % "cloudsim-plus" % "2.4.0",
      "redis.clients" % "jedis" % "2.9.0",
      "org.scalatest" %% "scalatest" % "3.0.2" % "test",
      "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0"
    )
  )