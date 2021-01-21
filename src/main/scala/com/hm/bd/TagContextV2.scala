//package com.hm.bd
//
//import com.hm.util.{JedisConn, LoggerKIiller, SparkHelper}
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.hbase.client.Put
//import org.apache.hadoop.hbase.io.ImmutableBytesWritable
//import org.apache.hadoop.hbase.mapred.TableOutputFormat
//import org.apache.hadoop.mapred.JobConf
//import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.{DataFrame, Row, SparkSession}
//import redis.clients.jedis.Jedis
//
//object TagContextV2 {
//  LoggerKIiller.killer()
//
//  def main(args: Array[String]): Unit = {
//    val spark: SparkSession = SparkHelper.getSparkSession
//    val config: Configuration = HbaseUtil.getHbase(spark: SparkSession)
//    val jobConf = new JobConf(config)
//    // 设置写入Hbase表的属性
//    jobConf.setOutputFormat(classOf[TableOutputFormat])
//    // 写入那张表
//    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "tags203")
//    // 读取数据
//    val df: DataFrame = spark.read.load("output/rs")
//    //获取用户id和标签组合的集合
//    val baseRDD: RDD[(VertexId, Seq[(String, Int)])] = df.rdd.mapPartitions((t: Iterator[Row]) => {
//      val jedis: Jedis = JedisConn.getJedis
//      t.map((row: Row) => {
//        //获取UserID
//        val userId: List[String] = TagUtil.getUserIDAll(row)
//        //获取标签数据
//        val adList: Seq[(String, Int)] = TagsAd.makeTags(jedis, row)
//        //返回userid,和用户标签集合的list
//        (userId, adList++userId.map((_,0)))
//      }).flatMap(t => {
//        var flag = true
//        t._1.map((uid: String) => {
//          //只需要一个id携带标签即可,其他的不用携带标签,防止冗余存储
//          if (flag) (uid.hashCode.toLong, t._2) else (uid.hashCode.toLong, List.empty)
//        })
//      })
//    })
//    //构造边
//    val edgeRDD: RDD[Edge[Int]] = df.rdd.flatMap((row: Row) => {
//      //获取userid
//      val list: List[String] = TagUtil.getUserIDAll(row)
//      list.map((t: String) => {
//        Edge(list.head.hashCode.toLong, t.hashCode.toLong, 0)
//      })
//    })
//    //构造图
//    val graph: Graph[List[(String, Int)], Int] = Graph(baseRDD, edgeRDD)
//    //取顶点
//    val vertices: VertexRDD[VertexId] = graph.connectedComponents().vertices
//    val joined: RDD[(VertexId, (VertexId, List[(String, Int)]))] = vertices.join(baseRDD)
//    joined.map((t: (VertexId, (VertexId, List[(String, Int)]))) => {
//      (t._2._1, t._2._2)
//    }).reduceByKey((list1: List[(String, Int)], list2: List[(String, Int)]) => {
//      val list: List[(String, Int)] = list1 ++ list2
//      //相同userid的进行分组
//      val grouped: Map[String, List[(String, Int)]] = list.groupBy(_._1)
//      //相同userid相同key的进行
//      grouped.mapValues((t: List[(String, Int)]) => t.map(_._2).sum).toList
//    }).map((t: (VertexId, List[(String, Int)])) => {
//      //设置rowkey
//      val rowkey = new Put(t._1.toString.getBytes())
//      val tags: String = t._2.mkString(",")
//      println(s"tags = ${tags}")
//      rowkey.addImmutable("tags".getBytes(), "20200120".getBytes(), tags.getBytes())
//      //创建一个零大小的序列。
//      (new ImmutableBytesWritable(), rowkey)
//    }) // 数据保存Hbase
//      .saveAsHadoopDataset(jobConf)
//  }
//
//}
//
