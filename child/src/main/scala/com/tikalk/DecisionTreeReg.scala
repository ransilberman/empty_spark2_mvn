package com.tikalk

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.DecisionTreeRegressor
import org.apache.spark.sql.SparkSession


object DecisionTreeReg {

  def dorun(spark: SparkSession, trainingFile: String, testFile: String): Unit = {

    // Load the data stored in LIBSVM format as a DataFrame.
    val trainingData = spark.read.format("libsvm").load(trainingFile)
    val testData = spark.read.format("libsvm").load(testFile)

    // Train a DecisionTree model.
    val dt = new DecisionTreeRegressor()
      .setMaxDepth(8)

    println("explain params: ", dt.explainParams())

    val model = dt.fit(trainingData)

    // Make predictions.
    val predictions = model.transform(testData)

    // Select example rows to display.
//    predictions.select("prediction", "features").show(5)

    // Select (prediction, true label) and compute test error.
//    val evaluator = new RegressionEvaluator()
//      .setLabelCol("label")
//      .setPredictionCol("prediction")
//      .setMetricName("rmse")
    val rmse = new RegressionEvaluator().evaluate(predictions)
    println(s"Root Mean Squared Error (RMSE) on test data = $rmse")

//    val treeModel = model.stages(1).asInstanceOf[DecisionTreeRegressionModel]
    println("Learned regression tree model:\n" + model.toDebugString)
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