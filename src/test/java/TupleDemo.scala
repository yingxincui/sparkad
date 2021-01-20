object TupleDemo {
  def main(args: Array[String]): Unit = {
    println(test1())//List(1, 2, 3, 4, 5, 6)
  }


  def test1(): List[Int] = {
    val list1 = List(1, 2, 3)
    val list2 = List(4, 5, 6)
    list1 ++ list2
  }

  def test2: Unit = {
    val list = List(
      (23, List(("a", 1), ("a", 1), ("b", 1))),
      (23, List(("a", 1), ("a", 1), ("b", 1))),
      (26, List(("a", 1), ("a", 1), ("b", 1)))
    )
  }

}
