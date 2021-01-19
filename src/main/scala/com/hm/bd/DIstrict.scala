package com.hm.bd


import com.hm.util.{LoggerKIiller, SparkHelper}
import org.apache.spark.sql.{DataFrame, SparkSession}

//for 制作地域分布报表
object DIstrict {
  LoggerKIiller.killer()
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkHelper.getSparkSession
    val df: DataFrame = spark.read.load("output/rs")
    df.createTempView("tmp")
    val sql: String =
      """
        |select
        |provincename,
        |cityname,
        |sum(if(requestmode=1 and processnode>=1,1,0)) as `原始请求`,
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
        |group by provincename,cityname
        |""".stripMargin
    spark.sql(sql).show()
  }
}
