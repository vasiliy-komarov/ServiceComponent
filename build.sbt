lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "ServiceComponent",
    scalaVersion := "2.12.1",
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.11" % "test",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.0.pr3",
      "commons-codec" % "commons-codec" % "1.9"
    )
  )