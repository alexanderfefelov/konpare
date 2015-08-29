name := """konpare"""

scalaVersion := "2.11.7"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, buildInfoBuildNumber,
      "builtBy" -> {System.getProperty("user.name")},
      "builtOn" -> {java.net.InetAddress.getLocalHost.getHostName},
      "builtAt" -> {new java.util.Date()},
      "builtAtMillis" -> {System.currentTimeMillis()}
    ),
    buildInfoPackage := "version"
  )

libraryDependencies ++= Seq(
)

fork in run := true

assemblyJarName in assembly := s"${name.value}.jar"
