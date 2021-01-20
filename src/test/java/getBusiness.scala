import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.commons.lang.StringUtils
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils

import java.util
import scala.collection.mutable.ListBuffer
import scala.tools.scalap.scalax.util.StringUtil
//测试利用经纬度获取商圈,需要首先去高德申请API
object getBusiness {
  def main(args: Array[String]): Unit = {
    val str: String = getBusinessFromMap(116.397128, 39.916527)
    println(str) //东单,北海,沙滩
  }

  def getBusinessFromMap(long: Double, lat: Double): String = {
    //调用高德地铁api利用经纬度获取地理信息
    val url: String = "https://restapi.amap.com/v3/geocode/regeo?output=json&location=" + long + "," + lat + "&key=94feff67a3b99ae4f15bd801b7f8508f"
    //Creates CloseableHttpClient instance with default configuration.
    val client: CloseableHttpClient = HttpClients.createDefault()
    val get = new HttpGet(url)
    val response: CloseableHttpResponse = client.execute(get)
    val str: String = EntityUtils.toString(response.getEntity, "UTF-8")
    println(str)
    //解析api返回的json数据
    val nObject: JSONObject = JSON.parseObject(str)
    if (nObject.getString("status") != "1") {
      return null
    }
    val regeocode: JSONObject = nObject.getJSONObject("regeocode")
    if (regeocode.isEmpty) return null
    val addressComponent: JSONObject = regeocode.getJSONObject("addressComponent")
    if (addressComponent.isEmpty) return null
    val business: JSONArray = addressComponent.getJSONArray("businessAreas")
    //[{"name":"东单","location":"116.416804,39.913479","id":"110101"},{"name":"北海","location":"116.387646,39.928173","id":"110102"},{"name":"沙滩","location":"116.402653,39.925923","id":"110101"}]
    println(business)
    if (business.isEmpty) return null
    //将商圈名字封装到list
    val list = new ListBuffer[String]()

    for (x <- business.toArray()) {
      //每个元素转为json对象
      val jarr: JSONObject = x.asInstanceOf[JSONObject]
      //提取商圈名称
      list.append(jarr.getString("name"))
    }
    list.mkString(",")

  }
}
