lazy val root = (project in file("."))
  .settings(
    name := "Alejandro Blanco-M",
    scalaVersion := "2.12.8",
    mainClass := Some("org.alecuba16.Main")
  )
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "org.mockito" % "mockito-all" % "1.8.4"