package com.jh.banno.routes

import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import com.google.inject.Inject
import com.jh.banno.service.WeatherService

import scala.concurrent.ExecutionContext

class ApiRouter @Inject()(weatherService: WeatherService)(implicit val ec: ExecutionContext) extends BaseRouter {

    def routes: Route = {
        versionRoutes("v1") {
            pathPrefix("weather") {
                respondWith(params =>
                    weatherService.getWeatherByLonLat(
                        lon = params.getDoubleOpt("lon").getOrElse(0.0),
                        lat = params.getDoubleOpt("lat").getOrElse(0.0)
                    )
                )
            }
        }
    }
}
