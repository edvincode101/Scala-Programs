import scala.collection.mutable

object HashMapExample {
  def mergeGrades(a: Map[String, Int], b: Map[String, Int]): Map[String, Int] = {
    (a.keySet ++ b.keySet).map { name =>
      val grade = (a.get(name), b.get(name)) match {
        case (Some(x), Some(y)) => math.max(x, y)
        case (Some(x), None) => x
        case (None, Some(y)) => y
        case _ => 0
      }
      name -> grade
    }.toMap
  }

  def main(args: Array[String]): Unit = {
    val students = mutable.HashMap[String, Int]()
    students("Anu") = 82
    students("Rahul") = 75
    students("Meera") = 91
    students("Anu") = 88
    println("Student grades: " + students)

    val first = Map("Anu" -> 82, "Rahul" -> 75)
    val second = Map("Anu" -> 88, "Meera" -> 91)
    println("Merged grades: " + mergeGrades(first, second))

    val chained = Map("groupA" -> List("Anu", "Arun"),
                      "groupB" -> List("Meera"))
    println("Chained values: " + chained)
  }
}
