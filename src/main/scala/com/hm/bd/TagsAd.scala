package com.hm.bd

import com.alibaba.fastjson.JSONObject
import com.hm.util.MapUtil
import org.apache.spark.sql.Row

import scala.collection.mutable.ListBuffer

object TagsAd {
  //给数据打标签
  //标签要求:1)	广告位类型（标签格式： LC03->1 或者 LC16->1）xx 为数字，小于 10 补 0，把广告位类型名称，LN 插屏->1
  def makeTags(args: Any*): Seq[(String, Int)] = {
    val row: Row = args(0).asInstanceOf[Row]
    var list = new ListBuffer[(String, Int)]()
    val adspacetype: Int = row.getAs[Int]("adspacetype")
    val adspacetypename: String = row.getAs[String]("adspacetypename")
    adspacetype match {
      case v if v >= 1 && v <= 9 => list.append(("LC0" + v, 1))
      case v => list.append(("LC" + v, 1))
    }
    if (adspacetypename != "") {
      list.append(("LN" + adspacetypename, 1))
    }

    //2)	App 名称（标签格式： APPxxxx->1）xxxx 为 App 名称，使用缓存文件 appname_dict 进行名称转换；APP 爱奇艺->1
    val appname: String =row.getAs[String]("appname")
    list.append(("APP"+appname,1))

    //3)	渠道（标签格式： CNxxxx->1）xxxx 为渠道 ID(adplatformproviderid)
    val adplatformproviderid: Int = row.getAs[Int]("adplatformproviderid")
    //追加渠道标签
    list.append(("CN" + adplatformproviderid, 1))

    //4)	设备：
    //a)	(操作系统 -> 1)
    //b)	(联网方 -> 1)
    //c)	(运营商 -> 1)
    val client: Int = row.getAs[Int]("client")
    val networkmannername: String = row.getAs[String]("networkmannername")
    val ispname: String = row.getAs[String]("ispname")
    client match {
      case 1 => list.append(("Android D00010001", 1))
      case 2 => list.append(("IOS D00010002", 1))
      case 3 => list.append(("WinPhone D00010003", 1))
      case _ => list.append(("_ 其 他 D00010004", 1))
    }
    networkmannername match {
      case "Wifi" => list.append(("WIFI D00020001", 1))
      case "4G" => list.append(("4G D00020002", 1))
      case "3G" => list.append(("3G D00020003", 1))
      case "2G" => list.append(("2G D00020004", 1))
      case _ => list.append(("_   D00020005", 1))
    }

    ispname match {
      case "移动" => list.append((("移动 D00030001", 1)))
      case "联通" => list.append((("联通 D00030002", 1)))
      case "电信" => list.append((("电信 D00030003", 1)))
      case _ => list.append((("_ D00030004", 1)))
    }

    //5)	关键字（标签格式：Kxxx->1）xxx 为关键字，关键字个数不能少于 3 个字符，且不能
    //超过 8 个字符；关键字中如包含‘‘|’’，则分割成数组，转化成多个关键字标签
    val keywords: String = row.getAs[String]("keywords")
    val arr: Array[String] = keywords.split("\\|")
    if (arr.length >= 3 && arr.length <= 8) {
      list.append(("K" + arr(0), 1))
    }

    //6)	地域标签（省标签格式：ZPxxx->1, 地市标签格式: ZCxxx->1）xxx 为省或市名称
    val provincename: String = row.getAs[String]("provincename")
    val cityname: String = row.getAs[String]("cityname")
    list.append(("ZP" + provincename, 1))
    list.append(("ZC" + cityname, 1))
    //7)	商圈标签
    val long: Long =row.getAs[Long]("long")
    val lat: Long =row.getAs[Long]("lat")
    val str: String = MapUtil.getBusinessFromMap(long, lat)





    list.toList
  }
}
