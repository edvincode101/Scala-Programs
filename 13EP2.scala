import java.awt.Desktop
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.saddle._
import vegas._

object Program23 {
  private def toSeries[A](values: Seq[A]): Series[Int, Any] =
    Series(values.zipWithIndex.map { case (value, index) => index -> (value: Any) }: _*)

  def main(args: Array[String]): Unit = {
    val dates = Vector(
      "2026-10-01",
      "2026-10-02",
      "2026-10-03",
      "2026-10-04",
      "2026-10-05",
      "2026-10-06"
    )
    val fruits = Vector("Apple", "Apple", "Banana", "Apple", "Orange", "Apple")
    val prices = Vector(120.0, 125.0, 60.0, 130.0, 90.0, 128.0)
    val quantities = Vector(10, 15, 20, 12, 18, 14)

    val frame = Frame(
      "Date" -> toSeries(dates),
      "Fruit" -> toSeries(fruits),
      "Price" -> toSeries(prices),
      "Quantity" -> toSeries(quantities)
    )

    println("Complete Fruit Dataset:")
    println(frame)

    val appleFrame = frame.rfilter(row => row.raw(1) == "Apple")
    println("\nApple Records:")
    println(appleFrame)

    val applePrices = appleFrame.colAt(2).toVec.toArray.map(_.asInstanceOf[Double])
    val averagePrice = applePrices.sum / applePrices.length
    println(f"\nAverage Apple Price: Rs. $averagePrice%.2f")

    val chartData = (0 until appleFrame.numRows).map { row =>
      Map[String, Any](
        "Date" -> appleFrame.colAt(0).raw(row).toString,
        "Price" -> applePrices(row)
      )
    }
    val chart = Vegas("Apple Price Trend")
      .withData(chartData)
      .encodeX("Date", Ord)
      .encodeY("Price", Quant)
      .mark(Line)

    val chartPath = Paths.get("apple_price_trend.html").toAbsolutePath
    Files.write(chartPath, chart.html.pageHTML().getBytes(StandardCharsets.UTF_8))
    println(s"\nVegas chart saved to: $chartPath")

    if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop.browse(chartPath.toUri)
    } else {
      println("Open the HTML file above in a browser to view the chart.")
    }
  }
}
