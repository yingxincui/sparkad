package com.hm.bd

import com.hm.util.SparkHelper
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object ProCityCount {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "D:\\source\\App\\Apachesoft\\hadoop-2.7.6")
    val spark: SparkSession = SparkHelper.getSparkSession
    import org.apache.spark.sql.functions._
    val df: DataFrame = spark.read.load("output/rs")
    df.groupBy("provincename","cityname").count().coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("provincename","cityname")
      .json("city/rs")

  }
}
