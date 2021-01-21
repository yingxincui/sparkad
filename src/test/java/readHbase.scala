import com.hm.util.SparkHelper
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object readHbase {
  def main(args: Array[String]): Unit = {
    //获取spark 入口
    val spark: SparkSession = SparkHelper.getSparkSession
    // 获取Hbase连接
    //使用HBase资源创建配置
    val conf: Configuration = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181")
    //配置读取表
    conf.set(TableInputFormat.INPUT_TABLE, "test20")
    //读取数据并转成RDD
    //获取给定Hadoop文件的RDD，其中包含任意新的API InputFormat和要传递到输入格式的额外配置选项。
    val hRDD: RDD[(ImmutableBytesWritable, Result)] = spark.sparkContext.newAPIHadoopRDD(
      conf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result])

    hRDD.foreach((t: (ImmutableBytesWritable, Result)) => {
      //获取rowkey
      val rowkey: String = Bytes.toString(t._2.getRow)
      //获取value,参数需要列族名和列名
      val value: String = Bytes.toString(t._2.getValue("cf".getBytes(), "2020".getBytes()))
      println(s"rowkey+value = ${rowkey + value}")
    })
  }
}
