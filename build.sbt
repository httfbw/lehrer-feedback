name := """feedback-app"""
organization := "de.httf"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "3.13.1"
