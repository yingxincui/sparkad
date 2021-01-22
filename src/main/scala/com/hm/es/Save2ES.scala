package com.hm.es


import com.hm.util.SparkHelper
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
//保存数据到ES进行可视化呈现
object Save2ES {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkHelper.getSparkSession
    val options = Map(
      //如果没有index,则新建
      "es.index.auto.create" -> "true",
      "es.nodes.wan.only" -> "true",
      //安装es的服务器节点
      "es.nodes" -> "192.168.10.101",
      //es的端口
      "es.port" -> "9200"
    )

    //读取数据成为DF
    val sourceDF: DataFrame = spark.read.load("output/rs")
    //DF可以直接写入ES
    sourceDF
      .write
      .format("org.elasticsearch.spark.sql")
      .options(options)
      .mode(SaveMode.Append)
      //index为hive_table
      .save("hive_table/docs")
  }
}
