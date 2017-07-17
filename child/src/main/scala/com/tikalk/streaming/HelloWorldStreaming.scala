package com.tikalk.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * In this example we read words from socket using "netcat" utility.
  * In order to provide data for streaming run the command:
  * $ nc -lk 9999
  * Then enter words and SPACE between the words
  */
object HelloWorldStreaming {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("HdfsWordCount")
   .setMaster("local[2]") //TODO: remove this line and add to VM options: -Dspark.master=local[2]
    // Create the context
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    // Create the Socket on the directory and use the stream to count words in new files created
    val lines = ssc.socketTextStream( "localhost", 9999)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
