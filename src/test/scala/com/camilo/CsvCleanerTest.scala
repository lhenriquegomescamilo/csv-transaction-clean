package com.camilo

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class CsvCleanerTest extends AnyFunSuite {

  test("groupByCategory should group items by category from a valid CSV file") {
    val resource = getClass.getResource("/input/transaction.categorization.test.csv")
    val path = Paths.get(resource.toURI).toAbsolutePath.toString
    val categories = new GroupByCategory(path)

    implicit val system: ActorSystem = ActorSystem("TestSystem")
    val groupedItemsFuture = categories.grouped()

    val result = Await.result(groupedItemsFuture, Duration(100000, TimeUnit.SECONDS))
    assert(result.size == 2)
  }

  test("it should do a simple test") {
    implicit val system: ActorSystem = ActorSystem("ActorSystemTest")
    val sinkUnderTest =
      Flow[Int].map(_ * 2).toMat(Sink.fold(0)(_ + _))(Keep.right)

    val future = Source(1 to 4).runWith(sinkUnderTest)
    val result = Await.result(future, Duration(3000, TimeUnit.SECONDS))
    assert(result == 20)
  }

  test("it should do a simple test with a different way using") {
    implicit val system: ActorSystem = ActorSystem("ActorSystemTest")
    val future = Source(1 to 4).runWith(Flow[Int].toMat(Sink.reduce(_ + _))(Keep.right))
    val result = Await.result(future, Duration(3000, TimeUnit.SECONDS))
    assert(result == 10)

  }
}

