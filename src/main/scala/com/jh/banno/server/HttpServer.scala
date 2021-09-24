package com.jh.banno.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.Directives.{complete, encodeResponse, extractUri, handleExceptions}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.jh.banno.server.exceptions.HttpServerError

object HttpServer {

    def apply(host: String, port: Int, routes: Route)
        (implicit system: ActorSystem, materializer: ActorMaterializer, timeout: Timeout): HttpServer = {
        new HttpServer(host, port, routes)
    }
}

class HttpServer (host: String, port: Int, routes: Route)
    (implicit system: ActorSystem, materializer: ActorMaterializer, timeout: Timeout) {

    val serverRoutes: Route = handleExceptions(exceptionHandler) {
        encodeResponse {
            routes
        }
    }

    def exceptionHandler: ExceptionHandler = ExceptionHandler {
        case e: HttpServerError => complete(e.toResponse)
        case t: Throwable =>
            extractUri { uri: Uri =>
                system.log.error(t, s"Unexpected error occurred serving uri [${uri.path}]")
                val error = new HttpServerError(StatusCodes.InternalServerError, "Unhandled error in application", t)
                complete(error.toResponse)
            }
    }

    Http().bindAndHandle(serverRoutes, host, port)
    system.log.info(s"Server online @ http://$host:$port/")

}
