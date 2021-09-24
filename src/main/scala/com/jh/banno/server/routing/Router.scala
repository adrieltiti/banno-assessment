package com.jh.banno.server.routing

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import play.api.libs.json.{JsValue, Json, Writes}

import scala.concurrent.{ExecutionContext}

trait Router {
    implicit val ec: ExecutionContext

    def routes: Route

    protected def parameterHandler(action: (ParameterBag) => Route): Route = {
        parameterMultiMap{ params =>
            val box = new ParameterBag(params)
            action(box)
        }
    }

    protected def jsonResponse(content: JsValue, status: StatusCode = StatusCodes.OK): HttpResponse = HttpResponse(
        status = status,
        entity = HttpEntity(ContentTypes.`application/json`, content.toString)
    )

    protected def notFound: HttpResponse = HttpResponse(
        status = StatusCodes.NotFound,
        entity = HttpEntity(ContentTypes.`application/json`, Json.obj("status" -> "Not Found").toString)
    )
}

