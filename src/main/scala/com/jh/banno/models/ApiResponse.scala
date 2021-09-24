package com.jh.banno.models

import akka.http.scaladsl.model.StatusCode
import play.api.libs.json.JsValue

trait ApiResponse {
    def status: StatusCode
    def toJson(fields: Set[String], raw: Boolean): JsValue
}
