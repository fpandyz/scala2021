name := "scala2021"

Compile/mainClass := Some("scala2021.${userlogin}.${tasknumber}.${ObjectName}")

scalaVersion := "2.13.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.1" % "test"
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-15" % "3.2.5.0" % "test"
libraryDependencies += "org.scalamock" %% "scalamock" % "5.1.0" % Test
libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0"
