package com.jh.banno.server.exceptions

import akka.http.scaladsl.model.StatusCodes

class InvalidParameter(paramName: String, mustBe: String)
    extends HttpServerError(
        code = StatusCodes.BadRequest,
        message = s"Invalid parameter $paramName, must be $mustBe",
        cause = new Exception(s"Invalid parameter $paramName, must be $mustBe")
    )