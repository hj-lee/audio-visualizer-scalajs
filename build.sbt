
enablePlugins(ScalaJSPlugin)

name := "audio-visualizer-scalajs"
version := "1.0"
scalaVersion := "2.11.8"
scalacOptions   ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-unchecked", "-Xlint", "-Yno-adapted-args", "-Ywarn-dead-code", "-Ywarn-value-discard" )


scalaJSUseRhino in Global := false

resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases") //add resolver

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"
libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.0"
libraryDependencies += "org.denigma" %%% "threejs-facade" % "0.0.74-0.1.7"
//libraryDependencies += "com.github.japgolly.scalajs-react" %%% "extra"   % "0.11.1"
//libraryDependencies += "com.lihaoyi"                       %%% "upickle" % "0.4.1"


skip in packageJSDependencies := false

jsDependencies += "org.webjars" % "jquery" % "3.1.0" / "3.1.0/jquery.js"
jsDependencies += ProvidedJS / "scripts/stats.min.js"

////
////   kissfft does not work included in -jsdeps.js
//jsDependencies += ProvidedJS / "scripts/kissfft/KissFFT.js"
//jsDependencies += ProvidedJS / "scripts/kissfft/FFT.js"


//jsDependencies += "org.webjars.bower" % "react" % "15.3.1" / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
//jsDependencies += "org.webjars.bower" % "react" % "15.3.1" / "react-dom.js" minified
//  "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM"


persistLauncher in Compile := true

persistLauncher in Test := false

/* move these files out of target/. Also sets up same file for both fast and full optimization */
val generatedDir = file("generated")
crossTarget  in (Compile, fullOptJS)                     := generatedDir
crossTarget  in (Compile, fastOptJS)                     := generatedDir
crossTarget  in (Compile, packageJSDependencies)         := generatedDir
crossTarget  in (Compile, packageScalaJSLauncher)        := generatedDir
crossTarget  in (Compile, packageMinifiedJSDependencies) := generatedDir

artifactPath in (Compile, fastOptJS)                     :=
  ((crossTarget in (Compile, fastOptJS)).value / ((moduleName in fastOptJS).value + "-opt.js"))

artifactPath in (Compile, packageJSDependencies)                     :=
  ((crossTarget in (Compile, packageJSDependencies)).value / ((moduleName in packageJSDependencies).value +
    "-jsdeps.min.js"))