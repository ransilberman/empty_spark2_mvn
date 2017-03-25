package com.tikalk

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel, DecisionTreeRegressor, LinearRegression}
import org.apache.spark.sql.SparkSession


object DecisionTreeRegPiepline {

  def dorun(spark: SparkSession, trainingFile: String, testFile: String): Unit = {

    // Load the data stored in LIBSVM format as a DataFrame.
    val data = spark.read.format("libsvm").load(trainingFile)

    val trainingData = spark.read.format("libsvm").load(trainingFile)
    val testData = spark.read.format("libsvm").load(testFile)

//    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))
//    trainingData.toDF().write.format("libsvm")save("training_data.txt")
//    testData.toDF().write.format("libsvm")save("test_data.txt")


    // Automatically identify categorical features, and index them.
    // Here, we treat features with > 4 distinct values as continuous.
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .fit(trainingData)

    // Train a DecisionTree model.
    val dt = new DecisionTreeRegressor()
      .setLabelCol("label")
      .setFeaturesCol("indexedFeatures")
//      .setMaxDepth(3)

    println("explain params: ", dt.explainParams())

    // Chain indexer and tree in a Pipeline.
    val pipeline = new Pipeline()
      .setStages(Array(featureIndexer, dt))

    // Train model. This also runs the indexer.
    val model = pipeline.fit(trainingData)

    // Make predictions.
    val predictions = model.transform(testData)

    // Select example rows to display.
    predictions.select("prediction", "label", "features").show(5)

    // Select (prediction, true label) and compute test error.
    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")
    val rmse = evaluator.evaluate(model.transform(trainingData))
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

    dorun(spark, "train2.txt", "test2.txt") //good correlation 10 features
//        dorun(spark, "train3.txt", "test3.txt") //bad correlation 10 features
    spark.stop()
  }

}