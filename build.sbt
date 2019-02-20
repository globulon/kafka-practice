import scala.language.postfixOps

organization  := "com.hbc"
name          := "kafka-demo"
version       := "0.4.1-SNAPSHOT"

scalaVersion        := "2.12.8"
crossScalaVersions  := List("2.11.12", "2.12.8")

def scalacOptionsVersion(scalaVersion: String): Seq[String] = CrossVersion.partialVersion(scalaVersion) match {
  case Some((2, scalaMajor)) if scalaMajor > 11  ⇒ Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding", "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros", // Allow macro definition (besides implementation and application)
    "-language:higherKinds", // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
//    "-Xfatal-warnings", // Fail the compilation if there are any warnings.
    "-Xfuture", // Turn on future language features.
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match", // Pattern match may not be typesafe.
    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ypartial-unification", // Enable partial unification in type constructor inference
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals", // Warn if a local definition is unused.
    "-Ywarn-unused:params", // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates", // Warn if a private member is unused.
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  )
  case _ ⇒ Seq(
    "-deprecation",
    "-encoding", "UTF-8",       
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",       
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ywarn-unused-import",
    "-Ypartial-unification" // Enable partial unification in type constructor inference
  )
}

scalacOptions ++= scalacOptionsVersion(scalaVersion.value)

libraryDependencies ++= Seq(
  "org.typelevel"               %% "cats-core"                  % "1.5.0",
  "org.typelevel"               %% "cats-effect"                % "1.1.0",
  "co.fs2"                      %% "fs2-core"                   % "1.0.1", // For cats 1.5.0 and cats-effect 1.1.0
  "co.fs2"                      %% "fs2-io"                     % "1.0.2",
  "org.typelevel"               %% "cats-mtl-core"              % "0.4.0",
  "com.typesafe"                %  "config"                     % "1.3.2",
  "com.chuusai"                 %% "shapeless"                  % "2.3.3",
  "com.olegpy"                  %% "meow-mtl"                   % "0.2.0",
  "com.amazonaws"               %  "aws-java-sdk-s3"            % "1.11.192"  % Provided,
  "org.slf4j"                   %  "slf4j-api"                  % "1.7.25",
  "ch.qos.logback"              %  "logback-core"               % "1.2.3"     % Test,
  "ch.qos.logback"              %  "logback-classic"            % "1.2.3"     % Test,
  "com.ironcorelabs"            %% "cats-scalatest"             % "2.4.0"     % Test,
  "org.scalactic"               %% "scalactic"                  % "3.0.5"     % Test ,
  "org.typelevel"               %% "cats-testkit"               % "1.5.0"     % Test,
  "org.scalatest"               %% "scalatest"                  % "3.0.5"     % Test,
  "org.typelevel"               %% "cats-laws"                  % "1.1.0"     % Test, //or `cats-testkit` if you are using ScalaTest
  "org.typelevel"               %% "cats-effect-laws"           % "1.1.0"     % Test,
  "org.typelevel"               %% "cats-mtl-laws"              % "0.4.0"     % Test,
  "com.github.alexarchambault"  %% "scalacheck-shapeless_1.13"  % "1.1.6"     % Test
) map { _ withSources() withJavadoc() }

fork in Test := true

addCompilerPlugin(dependency = "org.spire-math" %% "kind-projector" % "0.9.9")

resolvers ++= Seq(
  Resolver.typesafeRepo(status ="releases"),
  Resolver.sonatypeRepo(status = "releases"),
  Resolver.bintrayRepo(owner = "scalacenter", repo = "releases"),
  "Artifactory" at "https://hbc.jfrog.io/hbc/sbt-release/"
)

coverageEnabled                   := false
coverageEnabled in publishLocal   := false
coverageEnabled in publish        := false
coverageMinimum                   := 80

releaseVersionBump          := sbtrelease.Version.Bump.Next
releaseCrossBuild           := true
releaseIgnoreUntrackedFiles := true

publishTo := Some("Artifactory Realm" at "https://hbc.jfrog.io/hbc/sbt-release/")
credentials += Credentials("Artifactory Realm", "hbc.jfrog.io", System.getenv("JFROG_USER"), System.getenv("JFROG_PASSWD")) 
