import com.alibaba.fastjson.JSONObject

object Test2 {
  def main(args: Array[String]): Unit = {
    test1()
  }

  def test1(): Unit = {
    val nObject = new JSONObject()
    nObject.put("name", "xiaoming")
    nObject.put("age", 23)
    println(nObject)
  }

  def decode(): Unit = {
    val str = "{'name':'xiaoming','age':23}"
    //JSONObject.parseObject(str)

  }
}
