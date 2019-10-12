package pl.edu.agh.hyperflow.simulator.rest.api



import spray.json.DefaultJsonProtocol._
import spray.json.{JsNull, JsObject, JsString, JsValue, JsonReader, RootJsonFormat, deserializationError, enrichAny}

object SprayUtils {
  implicit class JsObjectOps(json: JsObject) {
    val fields = json.fields

    def get[T: JsonReader](key: String): T =
      fields.get(key) match {
        case Some(v) => v.convertTo[T]
        case None => deserializationError(s"No key ${key} in json ${json.toString}")
      }
  }

}
