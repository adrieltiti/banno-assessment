package com.jh.banno

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.cluster.Cluster
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.typed.ClusterSingleton
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.google.inject.Guice
import com.jh.banno.routes.MainRouter
import com.jh.banno.server.HttpServer
import com.jh.banno.support.{AppConfig, AppModule}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

object Main extends App with LazyLogging {

    val config = new AppConfig(ConfigFactory.load())

    implicit val system: ActorSystem = ActorSystem(config.systemName)
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val timeout: Timeout = config.requestTimeout
    implicit val executionContext: ExecutionContext = system.dispatcher

    // Akka typed and clustering
    implicit val typedSystem: akka.actor.typed.ActorSystem[Nothing] = system.toTyped
    implicit val cluster = Cluster(system)
    implicit val singletonManager: ClusterSingleton = ClusterSingleton(typedSystem)
    implicit val sharding: ClusterSharding = ClusterSharding(typedSystem)

    private val injector = Guice.createInjector(new AppModule(config))

    val apiRoutes: MainRouter = injector.getInstance(classOf[MainRouter])

    try {
        HttpServer(config.serverInterface, config.serverPort, apiRoutes.routes)
    } catch {
        case NonFatal(_) => system.terminate()
    }
}
