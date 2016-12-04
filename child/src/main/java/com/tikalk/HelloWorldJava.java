package com.tikalk;

import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

public class HelloWorldJava {

    public static void main(String[] args) {
        String logFile = "README.md"; // Should be some file on your system
        SparkConf conf = new SparkConf().setAppName("Word Count");
        conf.setMaster("local[2]"); //TODO: remove this line and add to VM options: -Dspark.master=local[2]
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> logData = sc.textFile(logFile).cache();

        long numAs = logData.filter(new Function<String, Boolean>() {
            public Boolean call(String s) { return s.contains("a"); }
        }).count();

        long numBs = logData.filter(new Function<String, Boolean>() {
            public Boolean call(String s) { return s.contains("b"); }
        }).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);

        sc.stop();
    }

}
