# Empty Spark + Maven project

You may use this project as a template to build Spark project using Maven

## Project Structure

This project contains all you need to start from scratch a new Spark project. 

It has the following folders:

* **/child/src/main/java** - A _Java_ **HelloWorld** example in Spark

* **/child/src/main/scala** - A _Scala_ folder with three programs:

  * **HelloWorldScala.scala** - A _Scala_ **HelloWorld** example in Spark

  * **HelloWorldSqlScala.scala** - A _Scala_ **HelloWorld** example in SparkSQL

  * **HelloWorldStreaming.scala** - A _Scala_ **HelloWorld** example in SparkStreaming

* **/child/src/test/scala** - A _Scalatest_ example of testing the code

## Getting Started

### Clone the project
```
$ git clone https://github.com/ransilberman/empty_spark2_mvn.git
```
### Build it
```
$ cd empty_spark2_mvn
$ mvn package
```

## Author

* **Ran Silberman** - [ransilberman.com](https://ransilberman.com/)



