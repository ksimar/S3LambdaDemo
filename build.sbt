name := "S3LambdaDemo"

version := "1.0"

scalaVersion := "2.12.1"

// https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-core
libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"
// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.265"
// https://mvnrepository.com/artifact/io.github.mkotsur/aws-lambda-scala
libraryDependencies += "io.github.mkotsur" %% "aws-lambda-scala" % "0.0.10"
// https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-events
libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "2.0.2"
