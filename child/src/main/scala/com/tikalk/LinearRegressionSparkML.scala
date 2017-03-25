package com.tikalk

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.ml.regression.LinearRegression


object LinearRegressionSparkML {

  def dorun(spark: SparkSession, trainingFile: String, testFile: String): Unit = {


    val training = spark.read.format("libsvm").load(trainingFile)
    val test = spark.read.format("libsvm").load(testFile)


    val lr = new LinearRegression()
      .setMaxIter(50)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)
    lrModel.transform(test).show

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

    dorun(spark, "training10features.txt", "test10features.txt") //good correlation 10 features
//    dorun(spark, "training10features2.txt", "test10features2.txt") //bad correlation 10 features
    spark.stop()
  }

}