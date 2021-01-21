import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes

object RowkeyTest {
  def main(args: Array[String]): Unit = {
    test3()
  }

  def test2(): Unit = {
    val bw = new ImmutableBytesWritable("apple".getBytes())
    val byte: Array[Byte] = bw.get()
    val str: String = Bytes.toString(byte)
    println(str) //apple
  }

  def test3(): Unit = {
    val bw = new ImmutableBytesWritable()
    val bytes: Array[Byte] = bw.get()
    println(Bytes.toString(bytes))
  }
}
