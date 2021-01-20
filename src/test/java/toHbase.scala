import com.hm.util.SparkHelper
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object toHbase {
  //spark读取数据写入Hbase测试
  def main(args: Array[String]): Unit = {
    //获取spark 入口
    val spark: SparkSession = SparkHelper.getSparkSession
    // 获取Hbase连接
    val config: Configuration = spark.sparkContext.hadoopConfiguration
    config.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181")
    //Construct a map/reduce job configuration.
    val jobConf = new JobConf(config)
    // 设置写入Hbase表的属性
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    // 写入表的名称
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "test20")
    val connection: Connection = ConnectionFactory.createConnection(config)
    val admin: Admin = connection.getAdmin
    if (!admin.tableExists(TableName.valueOf("test20"))) {
      //创建表test20
      val tableDescriptor = new HTableDescriptor(TableName.valueOf("test20"))
      //设置列簇名称
      val cf = new HColumnDescriptor("cf")
      tableDescriptor.addFamily(cf)
      admin.createTable(tableDescriptor)
      connection.close()
    }
    // 读取数据
    val df: DataFrame = spark.read.load("output/rs")
    import spark.implicits._
    //解析数据并存入hbase
    df.rdd.map((x: Row) => {
      val userId: String = x.getAs[String]("imei")
      val pro: String = x.getAs[String]("provincename")
      val city: String = x.getAs[String]("cityname")
      //以userid作为rowkey
      val rowkey = new Put(userId.getBytes())
      //列簇名cd,列名2020,value pro+city
      rowkey.addImmutable("cf".getBytes(), "2020".getBytes(), (pro + "," + city).getBytes())
      (new ImmutableBytesWritable(), rowkey)
    }).saveAsHadoopDataset(jobConf)
  }
}
