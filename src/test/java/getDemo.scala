import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils
//测试http get方法的应用
object getDemo extends App {
  val url = "https://git-scm.com/docs/git-stage"
  //使用默认配置创建CloseableHttpClient实例。
  val client: CloseableHttpClient = HttpClients.createDefault()
  val get = new HttpGet(url)
  //使用默认上下文执行HTTP请求。
  val response: CloseableHttpResponse = client.execute(get)
  //response.getEntity获取此响应的消息实体（如果有）。 该实体是通过调用setEntity提供的。
  private val result: String = EntityUtils.toString(response.getEntity, "UTF-8")
  println(result)
}
