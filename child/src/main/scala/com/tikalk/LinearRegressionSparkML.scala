package com.tikalk

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.ml.regression.LinearRegression


object LinearRegressionSparkML {

  def dorun(spark: SparkSession, housePricesFile: String): Unit = {


    val training = spark.read.format("libsvm")
      .load(housePricesFile)


    val lr = new LinearRegression()
      .setMaxIter(10)
      //reduce over-fitting by setting regularization parameter higher than 0.0:
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)

    // Print the coefficients and intercept for linear regression
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")


    // Summarize the model over the training set and print out some metrics
    val trainingSummary = lrModel.summary
    trainingSummary.residuals.show()
    println(s"numIterations: ${trainingSummary.totalIterations}")
    println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
    println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
    println(s"r2: ${trainingSummary.r2}")
  }

  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .master("local[2]") //TODO: remove this line and add to VM options: -Dspark.master=local[2]
      .appName("SQL count")
      .getOrCreate()

    dorun(spark, "train2.txt")
    spark.stop()
  }

}