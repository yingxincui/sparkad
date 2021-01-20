import ch.hsr.geohash.GeoHash

object GeoTest {
  def main(args: Array[String]): Unit = {
    //对经纬度进行编码
    val str: String = GeoHash.geoHashStringWithCharacterPrecision(39.916527, 116.397128, 6)
    println(str)//wx4g0d
    //解码还原为经纬度
    val hash: GeoHash = GeoHash.fromGeohashString(str)
    //1110011101001000111100000011000000000000000000000000000000000000
    // -> (39.9188232421875,116.38916015625) -> (39.913330078125,116.400146484375) -> wx4g0d
    println(hash)
  }
}
