package com.jh.banno.models

import play.api.libs.json.{Format, Json, Reads, Writes}

object WeatherModel {

    case class Clouds (
        all: Int
    )
    implicit val cloudsFormat: Format[Clouds] = Json.format[Clouds]

    case class Coord (
        lon: Double,
        lat: Double
    )
    implicit val coordFormat: Format[Coord] = Json.format[Coord]

    case class Main (
        temp: Double,
        feels_like: Double,
        temp_min: Double,
        temp_max: Double,
        pressure: Int,
        humidity: Int
    ) {
        /* It depends of units. Should be on other side.
         A Business rules engine should be used.
         Never hard coded
        */
        val temperatureClassification: String = temp match {
            case temp if temp > 78 => "Hot"
            case temp if temp <= 78  && temp >= 70 => "Moderate"
            case _ => "Cold"
        }
    }
    implicit val mainReads: Reads[Main] = Json.reads[Main]
    implicit val mainWrites = new Writes[Main] {
        def writes(main: Main) = Json.obj(
            "Temp" -> main.temp,
            "Temperature Classification" -> main.temperatureClassification,
            "Feels Like" -> main.feels_like,
            "Temp Min" -> main.temp_min,
            "Temp Max" -> main.temp_max,
            "Pressure" -> main.pressure,
            "Humidity" -> main.humidity
        )
    }


    case class Sys (
        id: Option[Int],
        country: String,
        sunrise: Int,
        sunset: Int
    )
    implicit val sysFormat: Format[Sys] = Json.format[Sys]

    case class Weather (
        id: Option[Int],
        main: String,
        description: String,
        icon: String
    )
    implicit val weatherFormat: Format[Weather] = Json.format[Weather]

    case class Wind (
        speed: Double,
        deg: Int
    )
    implicit val windFormat: Format[Wind] = Json.format[Wind]

    case class WeatherModel (
        coord: Coord,
        weather: Seq[Weather],
        base: String,
        main: Main,
        visibility: Int,
        wind: Wind,
        clouds: Clouds,
        dt: Int,
        sys: Sys,
        timezone: Int,
        id: Int,
        name: String,
        cod: Int
    )
    implicit val weatherModelFormat: Format[WeatherModel] = Json.format[WeatherModel]
    implicit val weatherReads: Reads[WeatherModel] = Json.reads[WeatherModel]

}
