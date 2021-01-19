package com.hm.bd

import com.hm.util.{LoggerKIiller, SparkHelper}
import org.apache.spark.sql.{DataFrame, SparkSession}

//3.2.2	终端设备运营
object Device {
  LoggerKIiller.killer()
  val spark: SparkSession = SparkHelper.getSparkSession

  def main(args: Array[String]): Unit = {
    //运营类指标统计
    yunying()
    wangluo()
    device()
    xitong()


  }

  def yunying(): Unit = {
    val df: DataFrame = spark.read.load("output/rs")
    df.createTempView("tmp")
    val sql: String =
      """
        |select
        |ispname,
        |count(1) as `总请求`,
        |sum(if(requestmode=1 and processnode>=2,1,0)) as `有效请求`,
        |sum(if(requestmode=1 and processnode=3,1,0)) as `广告请求`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `参与竞价数`,
        |sum(if(iseffective=1 and isbilling=1 and iswin=1 and adorderid!=0,1,0)) as `竞价成功数`,
        |sum(if(requestmode=2 and iseffective=1,1,0)) as `展示量`,
        |sum(if(requestmode=3 and iseffective=1,1,0)) as `点击量`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告成本`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告消费`
        |from tmp
        |where
        |group by ispname
        |""".stripMargin
    spark.sql(sql).show()
  }
  def wangluo(): Unit = {
    val df: DataFrame = spark.read.load("output/rs")
    df.createTempView("wangluo")
    val sql: String =
      """
        |select
        |networkmannername,
        |count(1) as `总请求`,
        |sum(if(requestmode=1 and processnode>=2,1,0)) as `有效请求`,
        |sum(if(requestmode=1 and processnode=3,1,0)) as `广告请求`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `参与竞价数`,
        |sum(if(iseffective=1 and isbilling=1 and iswin=1 and adorderid!=0,1,0)) as `竞价成功数`,
        |sum(if(requestmode=2 and iseffective=1,1,0)) as `展示量`,
        |sum(if(requestmode=3 and iseffective=1,1,0)) as `点击量`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告成本`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告消费`
        |from wangluo
        |where
        |group by networkmannername
        |""".stripMargin
    spark.sql(sql).show()
  }
  def device(): Unit = {
    val df: DataFrame = spark.read.load("output/rs")
    df.createTempView("shebei")
    val sql: String =
      """
        |select
        |device,
        |count(1) as `总请求`,
        |sum(if(requestmode=1 and processnode>=2,1,0)) as `有效请求`,
        |sum(if(requestmode=1 and processnode=3,1,0)) as `广告请求`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `参与竞价数`,
        |sum(if(iseffective=1 and isbilling=1 and iswin=1 and adorderid!=0,1,0)) as `竞价成功数`,
        |sum(if(requestmode=2 and iseffective=1,1,0)) as `展示量`,
        |sum(if(requestmode=3 and iseffective=1,1,0)) as `点击量`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告成本`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告消费`
        |from shebei
        |where
        |group by device
        |""".stripMargin
    spark.sql(sql).show()
  }
  def xitong(): Unit = {
    val df: DataFrame = spark.read.load("output/rs")
    df.createTempView("xitong")
    val sql: String =
      """
        |select
        |client,
        |count(1) as `总请求`,
        |sum(if(requestmode=1 and processnode>=2,1,0)) as `有效请求`,
        |sum(if(requestmode=1 and processnode=3,1,0)) as `广告请求`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `参与竞价数`,
        |sum(if(iseffective=1 and isbilling=1 and iswin=1 and adorderid!=0,1,0)) as `竞价成功数`,
        |sum(if(requestmode=2 and iseffective=1,1,0)) as `展示量`,
        |sum(if(requestmode=3 and iseffective=1,1,0)) as `点击量`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告成本`,
        |sum(if(iseffective=1 and isbilling=1 and isbid=1,1,0)) as `广告消费`
        |from xitong
        |where
        |group by client
        |""".stripMargin
    spark.sql(sql).show()
  }
}
