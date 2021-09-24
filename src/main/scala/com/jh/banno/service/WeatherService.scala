package com.jh.banno.service

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import com.jh.banno.core.CoreHttpService
import com.jh.banno.models.SimpleElementResponse
import com.jh.banno.models.WeatherModel.WeatherModel
import com.jh.banno.models.WeatherModel.weatherReads

case class WeatherServiceConfig(authToken: String, weatherUrl: String, lonPattern: String, latPattern: String, authPattern: String)

trait  WeatherService {
    def getWeatherByLonLat(lon: Double, lat: Double): Future[SimpleElementResponse[WeatherModel]]
}

class WeatherServiceImpl @Inject()(config: WeatherServiceConfig, core: CoreHttpService)(implicit val ec: ExecutionContext) extends WeatherService {

    def getWeatherByLonLat(lon: Double, lat: Double): Future[SimpleElementResponse[WeatherModel]] = {
        println(config.weatherUrl)
        val weatherUrl = config.weatherUrl
                        .replaceAll(config.lonPattern, lon.toString)
                        .replaceAll(config.latPattern, lat.toString)
                        .replaceAll(config.authPattern, config.authToken)

        core.getObjectOptFromJsonUrl[WeatherModel](weatherUrl)
                .map(res => SimpleElementResponse[WeatherModel](element = res))
    }

}
