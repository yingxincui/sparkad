package com.hm.util

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils

object MapUtil {
  def main(args: Array[String]): Unit = {
    println(getBusinessFromMap(1, 2))
  }

  def getBusinessFromMap(long: Double, lat: Double): String = {

    val url = "https://restapi.amap.com/v3/geocode/regeo?output=xml&location="+long+","+lat+"&key=94feff67a3b99ae4f15bd801b7f8508f"
    val client: CloseableHttpClient = HttpClients.createDefault()
    val get = new HttpGet(url)
    val response: CloseableHttpResponse = client.execute(get)
    EntityUtils.toString(response.getEntity, "UTF-8")
  }
}
