package com.camilo

import akka.actor.ActorSystem
import akka.stream.alpakka.csv.scaladsl.CsvParsing
import akka.stream.scaladsl.{FileIO, Keep, Sink}

import java.nio.file.Paths
import scala.collection.mutable
import scala.concurrent.Future

enum TransactionCategory {
  case TAXES extends TransactionCategory
  case RESTAURANT extends TransactionCategory
}

private val categoriesName = Map(
  "taxes" -> TransactionCategory.TAXES,
  "TAXES" -> TransactionCategory.TAXES,
  "restaurant" -> TransactionCategory.RESTAURANT,
  "RESTAURANT" -> TransactionCategory.RESTAURANT,
)

class CSVCleaner(filePath: String) {

}

class GroupByCategory(input: String) {

  implicit val system: ActorSystem = ActorSystem("ReadCategorization")

  def grouped(): Future[mutable.HashMap[TransactionCategory, List[Item]]] = {
    val store = mutable.HashMap[TransactionCategory, List[Item]]()
    val source = FileIO.fromPath(Paths.get(input))
      .via(CsvParsing.lineScanner())
      .map(_.map(_.utf8String))
      .map(_.flatMap(_.split(";")))
      .map {
        case List(category, description) => Item(category, description)
        case List(category, description, _) => Item(category, description)
        case _ => Item("", "")
      }

    val accumulator = Sink.fold[mutable.HashMap[TransactionCategory, List[Item]], Item](mutable.HashMap())((acc, current) =>
      categoriesName.get(current.category) match {
        case Some(category) =>
          val currentList = acc.getOrElse(category, List.empty)
          acc + (category -> (currentList :+ current))
        case _ => acc
      }
    )

    source.toMat(accumulator)(Keep.right).run()
  }

}

case class Item(category: String, description: String)

case class IndexCategorization(category: TransactionCategory, description: Set[String])