object CollectionsExample {
  def squareNumbers(numbers: List[Int]): List[Int] =
    numbers.map(n => n * n)

  def intersection(a: Set[Char], b: Set[Char]): Set[Char] =
    a.intersect(b)

  def main(args: Array[String]): Unit = {
    val primes = List(2, 3, 5, 7, 11)
    println("Prime numbers: " + primes)
    println("Squared numbers: " + squareNumbers(primes))

    val uniqueChars = "hello world".toSet
    println("Unique characters: " + uniqueChars)

    val setA = Set('a', 'b', 'c', 'd')
    val setB = Set('c', 'd', 'e', 'f')
    println("Intersection: " + intersection(setA, setB))

    val details = (101, "Anu", true)
    println("Integer: " + details._1)
    println("String: " + details._2)
    println("Boolean: " + details._3)
  }
}
