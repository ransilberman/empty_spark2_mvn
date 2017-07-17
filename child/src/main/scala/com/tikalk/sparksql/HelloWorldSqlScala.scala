package com.tikalk.sparksql

import org.apache.spark.sql.{Dataset, Row, SparkSession}

object HelloWorldSqlScala {

  def count(spark: SparkSession, logFile: String): Dataset[Row] = {

    val df = spark.read.format("com.databricks.spark.csv").option("header", "true")
      .option("mode","PERMISSIVE")
      .load(logFile)

    df.createOrReplaceTempView("names_data_view")

    val names: Dataset[Row] = spark.sql("SELECT * FROM names_data_view")

    names
  }

  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .master("local[2]") //TODO: remove this line and add to VM options: -Dspark.master=local[2]
      .appName("SQL count")
      .getOrCreate()

    val logFile = "names.csv" // Should be some file on your system
    val names = count(spark, logFile)
    names.show()
    spark.stop()
  }

}