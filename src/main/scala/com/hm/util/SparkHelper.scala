package com.hm.util

import org.apache.spark.sql.SparkSession

object SparkHelper {
  def getSparkSession: SparkSession = {
    System.setProperty("hadoop.home.dir", "D:\\source\\App\\Apachesoft\\hadoop-2.7.6")
    val spark: SparkSession = SparkSession.builder()
      .appName("test")
      .master("local")
      .getOrCreate()
    spark
  }
}
