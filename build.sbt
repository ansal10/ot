name := """ot"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "joda-time" % "joda-time" % "2.9.1",
  filters,
  "javax.mail" % "mail" % "1.5.0-b01",
  "org.mockito" % "mockito-all" % "2.0.2-beta",
  "org.powermock" % "powermock-api-mockito" % "1.6.3",
  "org.powermock" % "powermock-module-junit4" % "1.6.3"

)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
