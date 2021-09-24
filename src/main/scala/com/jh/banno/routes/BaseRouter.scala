package com.jh.banno.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.jh.banno.models.ApiResponse
import com.jh.banno.server.routing.{ParameterBag, Router}
import com.jh.banno.support.JsonModelManipulator
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{JsString, Json}

import scala.concurrent.Future
import scala.util.Try

trait BaseRouter extends Router with JsonModelManipulator with LazyLogging {

    protected def reportRoute(action: => Route): Route = {
        extractRequest { request =>
            action
        }
    }

    protected def respondWith(response: ParameterBag => Future[ApiResponse]): Route = parameterHandler { params =>
        complete(response(params).map(handleResponse(params)))
    }

    protected def versionRoutes(version: String)(action: => Route): Route =
        pathPrefix(version)(
            routesGroup(action)
        )

    protected def routesGroup(action: => Route): Route =
        ignoreTrailingSlash {
            get(
                reportRoute(action)
            )
        }

    protected def textResponse(content: String, status: StatusCode = StatusCodes.OK): HttpResponse = HttpResponse(
        status = status,
        entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, content)
    )

    private def handleResponse(params: ParameterBag)(resp: ApiResponse): HttpResponse = {
        val fields = params.getStringListOpt("fields").toSet
        val raw = params.getBooleanOpt("raw").getOrElse(false)
        val result = resp.toJson(fields, raw)
        result match {
            case JsString(value) => Try(Json.parse(value)).toOption
                .map(v => jsonResponse(v, resp.status))
                .getOrElse(textResponse(value, resp.status))
            case _ =>  jsonResponse(result, resp.status)
        }
    }
}
