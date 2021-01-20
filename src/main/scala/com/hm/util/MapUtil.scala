package com.hm.util

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils

import scala.collection.mutable.ListBuffer
//解析get请求
object MapUtil {
  def main(args: Array[String]): Unit = {
    println(getBusinessFromMap(1, 2))
  }

  def getBusinessFromMap(long: Double, lat: Double): String = {
    //调用高德地铁api利用经纬度获取地理信息
    val url: String = "https://restapi.amap.com/v3/geocode/regeo?output=json&location="+long+","+lat+"&key=94feff67a3b99ae4f15bd801b7f8508f"
    //Creates CloseableHttpClient instance with default configuration.
    val client: CloseableHttpClient = HttpClients.createDefault()
    val get = new HttpGet(url)
    val response: CloseableHttpResponse = client.execute(get)
    val str: String = EntityUtils.toString(response.getEntity, "UTF-8")
    val nObject: JSONObject = JSON.parseObject(str)
    if(nObject.getString("status")!="1"){
      return null
    }
    val regeocode: JSONObject = nObject.getJSONObject("regeocode")
    if(regeocode.isEmpty) return null
    val addressComponent: JSONObject = regeocode.getJSONObject("addressComponent")
    if(addressComponent.isEmpty) return null
    val business: JSONArray = addressComponent.getJSONArray("businessAreas")
    if(business.isEmpty) return null
    val list = new ListBuffer[String]()
    for(arr<-business.toArray()){
      val jarr: JSONObject = arr.asInstanceOf[JSONObject]
      list.append(jarr.getString("name"))
    }
    list.mkString(",")
  }
}
