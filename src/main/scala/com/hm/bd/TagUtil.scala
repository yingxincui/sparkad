package com.hm.bd

import org.apache.spark.sql.Row

import scala.collection.mutable.ListBuffer

object TagUtil {
  def getUserID(row: Row): String = {
    row match {
      case t if (t.getAs("imei") != "") => "IM: " + t.getAs("imei")
      case t if (t.getAs("mac") != "") => "IM: " + t.getAs("mac")
      case t if (t.getAs("idfa") != "") => "IM: " + t.getAs("idfa")
      case t if (t.getAs("openudid") != "") => "IM: " + t.getAs("openudid")
      case t if (t.getAs("androidid") != "") => "IM: " + t.getAs("androidid")
    }
  }

  //获取所有用户ID
  def getUserIDAll(row: Row): List[String] = {
    val list = new ListBuffer[String]()
    if (row.getAs("imei") != "") list.append("IM: " + row.getAs("imei"))
    if (row.getAs("mac") != "") list.append("IM: " + row.getAs("mac"))
    if (row.getAs("idfa") != "") list.append("IM: " + row.getAs("idfa"))
    if (row.getAs("openudid") != "") list.append("IM: " + row.getAs("openudid"))
    if (row.getAs("androidid") != "") list.append("IM: " + row.getAs("androidid"))
    list.toList
  }


}
