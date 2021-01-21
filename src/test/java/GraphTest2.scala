import com.hm.util.SparkHelper
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object GraphTest2 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkHelper.getSparkSession
    val sc: SparkContext = spark.sparkContext
    // 创建点RDD,每个点代表一个任
    val users: RDD[(VertexId, (String, String))] =
      sc.parallelize(Seq(
        (3L, ("rxin", "student")),
        (7L, ("jgonzal", "postdoc")),
        (5L, ("franklin", "prof")),
        (2L, ("istoica", "prof"))))
    // 创建边RDD,两点构成一条直线,第三个参数是这两个人的关系
    val relationships: RDD[Edge[String]] =
      sc.parallelize(Seq(
        Edge(3L, 7L, "collab"),
        Edge(5L, 3L, "advisor"),
        Edge(2L, 5L, "colleague"),
        Edge(5L, 7L, "pi")))
    // Define a default user in case there are relationship with missing user
    val defaultUser: (String, String) = ("John Doe", "Missing")
    // 构建图
    val graph: Graph[(String, String), String] = Graph(users, relationships, defaultUser)
    //计算每个顶点的连接组件成员资格，并返回一个顶点值包含包含该顶点的连接组件中最低顶点id的图。
    val vertices: VertexRDD[VertexId] = graph.connectedComponents().vertices

    // Count all users which are postdocs
    val count: VertexId = graph.vertices.filter { case (id, (name, pos)) => pos == "postdoc" }.count
    println(s"count = ${count}")
    // Count all the edges where src > dst
    graph.edges.filter((e: Edge[String]) => e.srcId > e.dstId).count

  }
}
