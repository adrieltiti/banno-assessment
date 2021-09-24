package com.jh.banno.support

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.typed.ClusterSingleton
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.google.inject.{AbstractModule, Provides, TypeLiteral}
import com.google.inject.name.Names
import com.jh.banno.service.{WeatherService, WeatherServiceConfig, WeatherServiceImpl}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

class AppModule(config: AppConfig)(implicit
    system: ActorSystem,
    typedSystem: akka.actor.typed.ActorSystem[Nothing],
    cluster: Cluster,
    singletonManager: ClusterSingleton,
    sharding: ClusterSharding,
    materializer: ActorMaterializer,
    timeout: Timeout,
    ec: ExecutionContext
) extends AbstractModule {

    def configure(): Unit = {
        bind(classOf[ActorSystem]).toInstance(system)
        bind(new TypeLiteral[akka.actor.typed.ActorSystem[Nothing]] {})
            .annotatedWith(Names.named("typed-system"))
            .toInstance(typedSystem)

        bind(classOf[Cluster]).toInstance(cluster)
        bind(classOf[ClusterSingleton]).toInstance(singletonManager)
        bind(classOf[ClusterSharding]).toInstance(sharding)
        bind(classOf[ActorMaterializer]).toInstance(materializer)
        bind(classOf[Timeout]).toInstance(timeout)
        bind(classOf[ExecutionContext]).toInstance(ec)

        // Configuration
        val weatherConfig: WeatherServiceConfig = WeatherServiceConfig(authToken = config.weatherAuthToken,
                                weatherUrl = config.weatherUrl,
                                lonPattern = config.LON_PATTERN,
                                latPattern = config.LAT_PATTERN,
                                authPattern = config.AUTH_TOKEN)

        bind(classOf[WeatherServiceConfig]).toInstance(weatherConfig)

        // Service
        bind(classOf[WeatherService]).to(classOf[WeatherServiceImpl]).asEagerSingleton()
    }
}
