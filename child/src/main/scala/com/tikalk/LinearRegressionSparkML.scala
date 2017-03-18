package com.tikalk

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.ml.regression.LinearRegression


object LinearRegressionSparkML {

  def dorun(spark: SparkSession, housePricesFile: String): Unit = {


    val training = spark.read.format("libsvm")
      .load(housePricesFile)


    val lr = new LinearRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)
    lrModel.transform(training).show

    // Print the coefficients and intercept for linear regression
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")


    // Summarize the model over the training set and print out some metrics
    val trainingSummary = lrModel.summary
    println(s"numIterations: ${trainingSummary.totalIterations}")
    println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
    println(s"RMSE: ${lrModel.summary.rootMeanSquaredError}")
  }

  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .master("local[2]") //TODO: remove this line and add to VM options: -Dspark.master=local[2]
      .appName("SQL count")
      .getOrCreate()

    dorun(spark, "train2.txt") //good correlation 10 features
//    dorun(spark, "train3.txt") //bad correlation 10 features
    spark.stop()
  }

}