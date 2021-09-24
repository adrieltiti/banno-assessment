package com.jh.banno.server.exceptions

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode}
import play.api.libs.json.Json

object HttpServerError {
    def apply(code: StatusCode, message: String, cause: Throwable): HttpServerError =
        new HttpServerError(code, message, cause)
    def apply(code: StatusCode, message: String): HttpServerError =
        new HttpServerError(code, message, new Exception(message))
}

class HttpServerError(val code: StatusCode, val message: String, cause: Throwable)
    extends Throwable(message, cause) {

    def toResponse: HttpResponse = HttpResponse(
        status = code,
        entity = HttpEntity(
            ContentTypes.`application/json`,
            Json.obj(
                "status" -> code.reason,
                "message" -> message
            ).toString
        )
    )

}
