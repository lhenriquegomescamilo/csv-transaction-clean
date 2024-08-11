package com.camilo

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}

object JediValueAkkaStream extends App {
  implicit val system: ActorSystem = ActorSystem("QuickStart")

  private val source = Source(1 to 100)
  private val flow = Flow[Int].map(_ * 2)
  private val sink = Sink.foreach[Int](println)

  source.via(flow).to(sink).run()
}
