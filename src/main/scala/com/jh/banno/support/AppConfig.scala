package com.jh.banno.support

import akka.util.Timeout
import scala.concurrent.duration._

class AppConfig(config: com.typesafe.config.Config) {

    val AUTH_TOKEN = "AUTH_TOKEN"
    val LON_PATTERN = "LON"
    val LAT_PATTERN = "LAT"

    private def getFiniteDuration(path: String): FiniteDuration = config.getDuration(path).toMillis.millis

    val systemName: String = config.getString("server.system-name")
    val serverInterface: String = config.getString("server.interface")
    val serverPort: Int = config.getInt("server.port")
    val requestTimeout: Timeout = getFiniteDuration("server.request-timeout")

    // Urls
    val weatherAuthToken: String = config.getString("weather.authToken")
    val weatherUrl: String = config.getString("weather.url")

}
