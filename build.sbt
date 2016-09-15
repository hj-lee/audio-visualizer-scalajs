
enablePlugins(ScalaJSPlugin)

name := "audio-visualizer-scalajs"

version := "1.0"

scalaVersion := "2.11.8"

scalaJSUseRhino in Global := false

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"

libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.0"

skip in packageJSDependencies := false
jsDependencies += "org.webjars" % "jquery" % "3.1.0" / "3.1.0/jquery.js"

// jsDependencies +=
resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases") //add resolver
libraryDependencies += "org.denigma" %%% "threejs-facade" % "0.0.74-0.1.7" //add dependency

persistLauncher in Compile := true

persistLauncher in Test := false

