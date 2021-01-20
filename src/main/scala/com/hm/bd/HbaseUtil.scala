package com.hm.bd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory}
import org.apache.spark.sql.SparkSession

object HbaseUtil {
  def getHbase(spark: SparkSession): Configuration = {
    // 获取Hbase连接
    val config: Configuration = spark.sparkContext.hadoopConfiguration
    config.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181")
    val connection: Connection = ConnectionFactory.createConnection(config)
    val admin: Admin = connection.getAdmin
    if (!admin.tableExists(TableName.valueOf("tags203"))) {
      val tableDescriptor = new HTableDescriptor(TableName.valueOf("tags203"))
      val cf = new HColumnDescriptor("tags")
      tableDescriptor.addFamily(cf)
      admin.createTable(tableDescriptor)
      connection.close()
    }
    config
  }
}
