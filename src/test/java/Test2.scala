import com.alibaba.fastjson.{JSON, JSONObject}
//scala处理json测试案例
object Test2 {
  def main(args: Array[String]): Unit = {
    //test1()
    //decode()
   //json2str()
    getarray()
  }

  def test1(): Unit = {
    val nObject = new JSONObject()
    nObject.put("name", "xiaoming")
    nObject.put("age", 23)
    println(nObject)
  }

  def decode(): Unit = {
    val str = "{'name':'xiaoming','age':23}"
    val nObject: JSONObject = JSON.parseObject(str)
    val str1: String = nObject.getString("name")
    println(s"str1 = ${str1}")//str1 = xiaoming
  }

  def json2str(): Unit = {
    val str = "{'name':'xiaoming','age':23}"
    val nObject: JSONObject = JSON.parseObject(str)
    val string: String = nObject.toJSONString
    println(s"string = ${string}")//string = {"name":"xiaoming","age":23}
  }

  def getarray(): Unit ={
    import com.alibaba.fastjson.JSONArray
    import com.alibaba.fastjson.JSONObject
    val str: String = "{\n" + "'name':'网站',\n" + "'num':3,\n" + "'sites':[ 'Google', 'Runoob', 'Taobao' ]\n" + "}"
    //字符串转json对象
    val jsonObject: JSONObject = JSON.parseObject(str)
    //将JSON对象转化为字符串
    val sites: String = jsonObject.getString("sites")
    //提取字符串中的数组
    val array: JSONArray = JSON.parseArray(sites)
    //获取数组的第一个元素
    System.out.println(array.get(0)) //Google
  }
}
