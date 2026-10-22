import java.awt.Desktop
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.saddle._
import spire.math.Rational
import vegas._

object FruitPriceStatistics {
  private case class PriceRecord(date: String, fruit: String, price: Double)

  private def rationalFromDecimal(value: Double): Rational = {
    val decimal = BigDecimal(value.toString)
    val denominator = BigInt(10).pow(decimal.scale)
    Rational(decimal.bigDecimal.unscaledValue, denominator)
  }

  private def mean(values: Seq[Rational]): Rational =
    values.foldLeft(Rational(0))(_ + _) / Rational(values.size)

  private def populationVariance(values: Seq[Rational]): Rational = {
    val average = mean(values)
    val squaredDifferences = values.map { price =>
      val difference = price - average
      difference * difference
    }
    squaredDifferences.foldLeft(Rational(0))(_ + _) / Rational(values.size)
  }

  private def toSeries(values: Seq[Any]): Series[Int, Any] =
    Series(values.zipWithIndex.map { case (value, index) => index -> value }: _*)

  private def savePriceChart(records: Seq[PriceRecord]): Unit = {
    val chartData = records.map { record =>
      Map[String, Any](
        "Date" -> record.date,
        "Fruit" -> record.fruit,
        "Price" -> record.price
      )
    }

    val chart = Vegas("Fruit Prices Over Time")
      .withData(chartData)
      .encodeX("Date", Ord)
      .encodeY("Price", Quant)
      .encodeColor("Fruit", Nom)
      .mark(Line)

    val chartPath = Paths.get("fruit_price_trends.html").toAbsolutePath
    Files.write(chartPath, chart.html.pageHTML().getBytes(StandardCharsets.UTF_8))
    println(s"Vegas chart saved to: $chartPath")

    if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop.browse(chartPath.toUri)
    } else {
      println("Open the HTML file above in a browser to view the chart.")
    }
  }

  def main(args: Array[String]): Unit = {
    val records = Seq(
      PriceRecord("2026-10-01", "Apple", 120.0),
      PriceRecord("2026-10-02", "Banana", 60.0),
      PriceRecord("2026-10-03", "Apple", 125.0),
      PriceRecord("2026-10-04", "Orange", 80.0),
      PriceRecord("2026-10-05", "Apple", 130.0),
      PriceRecord("2026-10-06", "Banana", 62.0),
      PriceRecord("2026-10-07", "Orange", 82.0)
    )

    val frame = Frame(
      "Date" -> toSeries(records.map(_.date)),
      "Fruit" -> toSeries(records.map(_.fruit)),
      "Price" -> toSeries(records.map(_.price))
    )
    println("Fruit price records:")
    println(frame)

    val pricesByRow = frame.colAt(2).toVec.toArray.map(_.asInstanceOf[Double])
    val groupedPrices = frame
      .groupBy(index => records(index).fruit)
      .groups
      .toSeq
      .sortBy(_._1)

    println("\nPrice statistics by fruit:")
    groupedPrices.foreach { case (fruit, indices) =>
      val prices = indices.map(index => rationalFromDecimal(pricesByRow(index)))
      val average = mean(prices)
      val variance = populationVariance(prices)
      println(f"$fruit%-8s Mean: ${average.toDouble}%.2f, Population variance: ${variance.toDouble}%.2f")
    }

    savePriceChart(records)
  }
}
