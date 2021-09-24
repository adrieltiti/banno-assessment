package com.jh.banno.core

import java.nio.charset.StandardCharsets
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse, StatusCodes}
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{JsResultException, Json, Reads}


class CoreHttpService @Inject()(implicit system: ActorSystem, val ec: ExecutionContext) extends LazyLogging {

    def getObjectOptFromJsonUrl[T](url: String)(implicit r: Reads[T]): Future[Option[T]] = {

        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = url))
        responseFuture.map(response => {
            response.status match {
                case StatusCodes.OK => {
                    val jsonObject = Json.parse(response.entity.asInstanceOf[HttpEntity.Strict].getData().decodeString(StandardCharsets.UTF_8))

                    Try(jsonObject.as[T]) match {
                        case Success(value) => Some(value)
                        case Failure(exception) => logger.error("A Failure Happens: ", exception.toString)
                                                   None
                    }
                }
                case _ => None
            }
        })
    }
}
