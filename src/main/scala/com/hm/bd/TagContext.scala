package com.hm.bd

import com.hm.util.{LoggerKIiller, SparkHelper}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object TagContext {
  LoggerKIiller.killer()

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkHelper.getSparkSession
    val df: DataFrame = spark.read.load(("output/rs"))
    //获取标签字段
    val value: RDD[(String, Seq[(String, Int)])] = df.rdd.map((row: Row) => {
      val userId: String = TagUtil.getUserID(row)
      //广告位类型标签
      val adList: Seq[(String, Int)] = TagsAd.makeTags(row)
      (userId, adList)
    })
    value.foreach(println)
  }
}
