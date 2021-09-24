package com.jh.banno.support

import play.api.libs.json.{JsArray, JsNull, JsObject, JsValue}

trait JsonModelManipulator {

    def manipulateJsonModel(fields: Set[String])(model: JsValue): JsValue = model match {
        case list: JsArray => JsArray(list.value.toList.map(manipulateJsonModel(fields)))
        case obj: JsObject => JsObject(obj.fields.flatMap(manipulateJsonField(fields)))
        case _ => model
    }

    private def manipulateJsonField(fields: Set[String])(field: (String, JsValue)): Option[(String, JsValue)] = {
        val (name, value) = field
        (fields.isEmpty, fields.contains(name.toLowerCase), value) match {
            case (_, _, JsNull) => None
            case (false, false, _) => None
            case (_, _, _: JsArray) => Some(name -> manipulateJsonModel(Set.empty)(value))
            case _ => Some(field)
        }
    }




}