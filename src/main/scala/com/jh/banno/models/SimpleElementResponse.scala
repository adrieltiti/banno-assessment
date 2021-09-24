package com.jh.banno.models

import play.api.libs.json._
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import com.jh.banno.support.JsonModelManipulator

case class SimpleElementResponse[A](element: Option[A])(implicit writes: Writes[A])
    extends ApiResponse with JsonModelManipulator {

    def status: StatusCode = if (element.isDefined) StatusCodes.OK else StatusCodes.NotFound

    def toJson(fields: Set[String], raw: Boolean): JsValue = Json.toJson(element
        .map(element => Json.toJson(element))
        .map(manipulateJsonModel(fields))
        .map(applyRaw(raw))
        .getOrElse(notFoundJson))

    def applyRaw(raw: Boolean)(json: JsValue): JsValue = json match {
        case JsObject(items) if raw => items.headOption.map{ case (_, value) => value}.getOrElse(JsNull)
        case value => value
    }

    def notFoundJson: JsValue = Json.obj(
        "code" -> "NOT_FOUND"
    )

}
