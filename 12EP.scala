object FunctionalCombinators {
  def main(args: Array[String]): Unit = {
    val numbers = (1 to 10).toList
    val evens = numbers.filter(_ % 2 == 0)
    val tripled = numbers.map(_ * 3)
    val pairs = numbers.flatMap(n => List((n, n * n)))
    val product = numbers.foldLeft(1)(_ * _)

    println("Original list: " + numbers)
    println("Even numbers: " + evens)
    println("Tripled: " + tripled)
    println("Number-square tuples: " + pairs)
    println("Product: " + product)
  }
}
