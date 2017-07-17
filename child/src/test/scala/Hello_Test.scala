package com.tikalk

import java.io.File

import com.tikalk.sparksql.HelloWorldSqlScala
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * Created by rans on 04/12/16.
  */
class Hello_Test extends FlatSpec with BeforeAndAfter with Matchers{
  Logger.getLogger("org.apache.spark").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  implicit val rowEncoder = org.apache.spark.sql.Encoders.kryo[Row]

  private val master = "local[2]"
  private val appName = "example-spark"
  private var spark: SparkSession = _

  before {
    spark = SparkSession
      .builder()
      .master(master)
      .appName("Word Test")
      .getOrCreate()

  }

  after {
    if (spark != null) {
      spark.stop()
    }
  }

  "Count of existing file" should "show correct num of lines" in{
    val names : Dataset[Row] = HelloWorldSqlScala.count(spark, "names.csv")
    assert(names != null)
    assert(names.count() == 3)
  }

  /**
    * test to check for exception
    */
  "Count of missing file" should "throw Exception" in {

    try {
      val names : Dataset[Row] = HelloWorldSqlScala.count(spark, "Muk.csv")
      fail //if exception is not catched than test should fail!
    } catch {
      case _:org.apache.spark.sql.AnalysisException => //expected exception
    }
  }

}