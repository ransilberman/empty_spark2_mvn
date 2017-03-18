package com.tikalk

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel, DecisionTreeRegressor, LinearRegression}
import org.apache.spark.sql.SparkSession


object DecisionTreeRegression {

  def dorun(spark: SparkSession, logfile: String): Unit = {

    // Load the data stored in LIBSVM format as a DataFrame.
    val data = spark.read.format("libsvm").load(logfile)

    // Automatically identify categorical features, and index them.
    // Here, we treat features with > 4 distinct values as continuous.
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4)
      .fit(data)

    // Split the data into training and test sets (30% held out for testing).
    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    // Train a DecisionTree model.
    val dt = new DecisionTreeRegressor()
      .setLabelCol("label")
      .setFeaturesCol("indexedFeatures")

    println("explain params: ", dt.explainParams())

    // Chain indexer and tree in a Pipeline.
    val pipeline = new Pipeline()
      .setStages(Array(featureIndexer, dt))

    // Train model. This also runs the indexer.
    val model = pipeline.fit(trainingData)
//    val model = pipeline.fit(data)

    // Make predictions.
    val predictions = model.transform(testData)
//    val predictions = model.transform(data)

    // Select example rows to display.
    predictions.select("prediction", "label", "features").show(5)

    // Select (prediction, true label) and compute test error.
    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")
    val rmse = evaluator.evaluate(predictions)
    println("Root Mean Squared Error (RMSE) on test data = " + rmse)

    val treeModel = model.stages(1).asInstanceOf[DecisionTreeRegressionModel]
    println("Learned regression tree model:\n" + treeModel.toDebugString)
  }

  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .master("local[2]") //TODO: remove this line and add to VM options: -Dspark.master=local[2]
      .appName("SQL count")
      .getOrCreate()

    dorun(spark, "train2.txt") //good correlation 10 features
//        dorun(spark, "train3.txt") //bad correlation 10 features
    spark.stop()
  }

}