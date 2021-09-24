lazy val root = (project in file("."))
  .enablePlugins(Nil: _*)
  .settings(
    organization        := "com.jh.banno",
    scalaVersion        := "2.12.10",
    name                := "banno-assessment",
    libraryDependencies ++= Dependencies.libraryDependencies,
    resolvers           := Dependencies.resolvers
  )
