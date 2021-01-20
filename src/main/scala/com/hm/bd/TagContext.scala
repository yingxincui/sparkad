package com.hm.bd

import com.hm.util.{JedisConn, LoggerKIiller, SparkHelper}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import redis.clients.jedis.Jedis

object TagContext {
  LoggerKIiller.killer()

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkHelper.getSparkSession
    import spark.implicits._
    val config: Configuration = HbaseUtil.getHbase(spark: SparkSession)
    val jobConf = new JobConf(config)
    // 设置写入Hbase表的属性
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    // 写入那张表
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "tags203")
    // 读取数据
    val df: DataFrame = spark.read.load("output/rs")
    //获取用户id和标签组合的集合
    df.rdd.mapPartitions((t: Iterator[Row]) => {
      val jedis: Jedis = JedisConn.getJedis
      t.map((row: Row) => {
        //获取UserID
        val userId: String = TagUtil.getUserID(row)
        //获取标签数据
        val adList: Seq[(String, Int)] = TagsAd.makeTags(jedis, row)
        //返回userid,和用户标签集合的list
        (userId, adList.toList)
      })
    }).reduceByKey((list1: List[(String, Int)], list2: List[(String, Int)]) => {
      val list: List[(String, Int)] = list1 ++ list2
      //相同userid的进行分组
      val grouped: Map[String, List[(String, Int)]] = list.groupBy(_._1)
      //相同userid相同key的进行
      grouped.mapValues((t: List[(String, Int)]) => t.map(_._2).sum).toList
    }).map((t: (String, List[(String, Int)])) => {
      //设置rowkey
      val rowkey = new Put(t._1.getBytes())
      val tags: String = t._2.mkString(",")
      println(s"tags = ${tags}")
      rowkey.addImmutable("tags".getBytes(), "20200120".getBytes(), tags.getBytes())
      (new ImmutableBytesWritable(), rowkey)
    }) // 数据保存Hbase
      .saveAsHadoopDataset(jobConf)
  }

}

