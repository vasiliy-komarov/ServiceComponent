lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "ServiceComponent",
    scalaVersion := "2.12.1",
    libraryDependencies ++= Seq(
      //"org.apache.httpcomponents" % "httpcore" % "4.4.3",
      "junit" % "junit" % "4.11" % "test",
//      "org.mapdb" % "mapdb" % "3.0.4",
      "commons-codec" % "commons-codec" % "1.9"
    )
  )