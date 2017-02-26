# Empty Spark + Maven project

You may use this project as a template to build Spark project using Maven

## Project Structure

This project contains all you need to start from scratch a new Spark project
It has the following folders:

* **/child/src/main/java** - A Java **HelloWorld** example in Spark
* **/child/src/main/scala** - A Scala folder with three programs:
  * **HelloWorldScala.scala** - A Scala **HelloWorld** example in Spark
  * **HelloWorldSqlScala.scala** - A Scala **HelloWorld** example in SparkSQL
  * **HelloWorldStreaming.scala** - A Scala **HelloWorld** example in SparkStreaming
* **/child/src/test/scala** - A Scalatest exaple of testing the code

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



