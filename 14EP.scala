//> using dep "org.scalanlp::breeze:2.1.0"

import breeze.linalg._
import java.awt.Desktop
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

object FruitRevenue {
  private def jsonString(value: String): String =
    "\"" + value.flatMap {
      case '"' => "\\\""
      case '\\' => "\\\\"
      case c if c < ' ' => f"\\u${c.toInt}%04x"
      case c => c.toString
    } + "\""

  private def showBarChart(totals: Seq[(String, Double)]): Unit = {
    val labels = totals.map { case (fruit, _) => jsonString(fruit) }.mkString("[", ",", "]")
    val values = totals.map { case (_, revenue) => java.lang.Double.toString(revenue) }
      .mkString("[", ",", "]")
    val html =
      s"""<!doctype html>
         |<html lang="en">
         |<head>
         |  <meta charset="utf-8">
         |  <meta name="viewport" content="width=device-width, initial-scale=1">
         |  <title>Fruit Revenue</title>
         |  <script src="https://cdn.plot.ly/plotly-2.35.2.min.js"></script>
         |</head>
         |<body>
         |  <div id="chart" style="width: 100%; height: 90vh;"></div>
         |  <script>
         |    Plotly.newPlot("chart", [{
         |      x: $labels,
         |      y: $values,
         |      type: "bar",
         |      marker: { color: "#4c78a8" }
         |    }], {
         |      title: "Total Revenue by Fruit",
         |      xaxis: { title: "Fruit" },
         |      yaxis: { title: "Revenue (Rs.)" }
         |    });
         |  </script>
         |</body>
         |</html>
         |""".stripMargin

    val chartPath = Paths.get("fruit_revenue_chart.html").toAbsolutePath
    Files.write(chartPath, html.getBytes(StandardCharsets.UTF_8))
    println(s"Plotly bar chart saved to: $chartPath")

    if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop.browse(chartPath.toUri)
    } else {
      println("Open the HTML file above in a browser to view the chart.")
    }
  }

  def main(args: Array[String]): Unit = {
    val fruits = Array("Apple", "Banana", "Orange", "Apple", "Banana", "Orange")
    val prices = DenseVector(120.0, 60.0, 80.0, 125.0, 62.0, 82.0)
    val quantities = DenseVector(10.0, 15.0, 12.0, 8.0, 14.0, 9.0)

    val revenue = prices *:* quantities
    println("Revenue per record:")
    println(revenue)

    val totals = fruits.indices.groupBy(fruits(_)).map {
      case (fruit, indices) => fruit -> indices.map(revenue(_)).sum
    }
    println("Total revenue by fruit:")
    val sortedTotals = totals.toSeq.sortBy(_._1)
    sortedTotals.foreach(println)
    showBarChart(sortedTotals)
  }
}
