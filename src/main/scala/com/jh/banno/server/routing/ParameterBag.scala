package com.jh.banno.server.routing


import scala.util.{Failure, Success, Try}
import com.jh.banno.server.exceptions.InvalidParameter
import com.typesafe.scalalogging.LazyLogging

class ParameterBag (rawParameters: Map[String, List[String]]) extends LazyLogging {

    val params: Map[String, List[String]] = rawParameters
        .map{ case (name, values) =>
            name.trim.toLowerCase -> values.flatMap(_.split(",")).map(_.toLowerCase.trim).filter(_.nonEmpty)
        }
        .filter{ case (_, values) => values.nonEmpty}

    def getStringListOpt(name: String): List[String] =
        params.getOrElse(name.trim.toLowerCase, Nil)

    def getStringOpt(name: String): Option[String] =
        getStringListOpt(name).headOption

    def getDoubleOpt(name: String): Option[Double] = getStringOpt(name).flatMap(value => parseDoubleOpt(name, value))

    def getBooleanOpt(name: String): Option[Boolean] = getStringOpt(name).map(parseBoolean(name))

    private def parseDoubleOpt(name: String, value: String): Option[Double] = Try(value.toDouble) match {
        case Success(res) => Some(res)
        case Failure(_) =>
            logger.warn("""Invalid parameter {} with value "{}", must be Long""", name, value)
            None
    }

    private def parseBoolean(name: String)(value: String): Boolean = value.trim.toLowerCase match {
        case "true" => true
        case "false" => false
        case _ => throw new InvalidParameter(name, "Boolean")
    }

}
