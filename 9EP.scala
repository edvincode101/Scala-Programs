
object ArrayExample {
  def sumArray(arr: Array[Int]): Int = {
    arr.sum
  }
  def concatenateFruits(fruits: Array[String]): String = {
    fruits.mkString(", ")
  }
  def main(args: Array[String]): Unit = {
    val numbers = Array(2, 5, 9, 14, 20)
    val fruits = Array("Apple", "Banana", "Mango", "Orange", "Grapes")
    println("Sum of array elements: " + sumArray(numbers))
    println("Fruits: " + concatenateFruits(fruits))
    println("Integer array elements:")
    for (num <- numbers) {
      println(num)
    }
  }
}
