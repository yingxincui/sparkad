import com.hm.util.SparkHelper
import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object GraphTest {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkHelper.getSparkSession
    //构造点集合
    val vertexRDD: RDD[(Long, (String, Int))] = spark.sparkContext.makeRDD(Seq(
      (1L, ("李白", 44)),
      (2L, ("李四", 23)),
      (6L, ("王五", 26)),
      (9L, ("赵六", 66)),
      (16L, ("吕布", 44)),
      (21L, ("刘备", 34)),
      (44L, ("张飞", 24)),
      (5L, ("宋江", 44)),
      (7L, ("李逵", 30)),
      (133L, ("小米", 24)),
      (138L, ("Nokia", 22)),
      (158L, ("坚果", 29))
    ))
    //构造边
    val edgeRDD: RDD[Edge[Int]] = spark.sparkContext.makeRDD(Seq(
      Edge(1L, 133L, 0),
      Edge(9L, 133L, 0),
      Edge(6L, 133L, 0),
      Edge(2L, 133L, 0),
      Edge(6L, 138L, 0),
      Edge(16L, 138L, 0),
      Edge(21L, 138L, 0),
      Edge(44L, 138L, 0),
      Edge(5L, 158L, 0),
      Edge(7L, 158L, 0)
    ))
    //调用图计算技术实现连通图
    val graph: Graph[(String, Int), Int] = Graph(vertexRDD, edgeRDD)
    //获取顶点,返回包含顶点及其相关属性的RDD。
    val vertices: VertexRDD[VertexId] = graph.connectedComponents().vertices
    //(21,1)
    //(16,1)
    //(158,5)
    //(138,1)
    //(133,1)
    //(1,1)
    //(6,1)
    //(7,5)
    //(9,1)
    //(44,1)
    //(5,5)
    //(2,1)
    vertices.foreach(println)
    //将每一个点的数据进行join操作
    //返回一个RDD，该RDD包含具有匹配键的所有元素对。
    //每对元素将作为（k，（v1，v2））元组返回，其中（k，v1）在这个元组中，（k，v2）在另一个元组中。使用给定的分区器对输出RDD进行分区。
    val joined: RDD[(VertexId, (VertexId, (String, Int)))] = vertices.join(vertexRDD)
    //(21,(1,(刘备,34)))
    //(16,(1,(吕布,44)))
    //(158,(5,(坚果,29)))
    //(138,(1,(Nokia,22)))
    //(133,(1,(小米,24)))
    //(1,(1,(李白,44)))
    //(6,(1,(王五,26)))
    //(7,(5,(李逵,30)))
    //(9,(1,(赵六,66)))
    //(44,(1,(张飞,24)))
    //(5,(5,(宋江,44)))
    //(2,(1,(李四,23)))
    joined.foreach(println)

    joined.map((t: (VertexId, (VertexId, (String, Int)))) => {
      (t._2._1, List(t._2._2))
      //(1,List((刘备,34), (吕布,44), (Nokia,22), (小米,24), (李白,44), (王五,26), (赵六,66), (张飞,24), (李四,23)))
      //(5,List((坚果,29), (李逵,30), (宋江,44)))
    }).reduceByKey(_ ++ _).foreach(println)
  }
}
