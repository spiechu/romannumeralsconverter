name := "romannumeralsconverter"

organization := "pl.spiechu"

version := "0.1"

scalaVersion := "2.11.1"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test"

scalacOptions += "-deprecation"

instrumentSettings

CoverallsPlugin.coverallsSettings