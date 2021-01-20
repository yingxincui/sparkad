package com.hm.bd

import com.hm.util.{LoggerKIiller, SparkHelper}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object TagContext {
  LoggerKIiller.killer()

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkHelper.getSparkSession

    val config: Configuration = Hbase(spark)

    val jobconf = new JobConf(config)
    jobconf.setOutputFormat()



    val df: DataFrame = spark.read.load(("output/rs"))
    //获取标签字段
    val value: RDD[(String, Seq[(String, Int)])] = df.rdd.map((row: Row) => {
      val userId: String = TagUtil.getUserID(row)
      //广告位类型标签
      val adList: Seq[(String, Int)] = TagsAd.makeTags(row)
      (userId, adList)
    }).reduceByKey((list1: Seq[(String, Int)], list2: Seq[(String, Int)]) => {
      val tuples: Seq[(String, Int)] = list1 ++ list2
      val grouped: Map[String, Seq[(String, Int)]] = tuples.groupBy(_._1)
      val map: Map[String, Int] = grouped.mapValues((t: Seq[(String, Int)]) => t.map(_._2).sum)
      map.toList

    })
    value.map((t: (String, Seq[(String, Int)])) =>{
      val rowkey = new Put(t._1.getBytes())
      val tags: String = t._2.mkString(",")
      val put: Put = rowkey.addImmutable("tags".getBytes(), "20210120".getBytes(), tags.getBytes())
      (new ImmutableBytesWritable(),put)
    }).saveAsHadoopDataset(jobconf)
  }

  private def Hbase(spark: SparkSession): Configuration = {
    // 获取Hbase连接
    val config: Configuration = spark.sparkContext.hadoopConfiguration
    config.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181")
    val connection: Connection = ConnectionFactory.createConnection(config)
    val admin: Admin = connection.getAdmin
    if (!admin.tableExists(TableName.valueOf("tags"))) {
      val tableDescriptor = new HTableDescriptor(TableName.valueOf("tags"))
      val cf = new HColumnDescriptor("tags")
      tableDescriptor.addFamily(cf)
      admin.createTable(tableDescriptor)
      connection.close()
    }
    config
  }
}
