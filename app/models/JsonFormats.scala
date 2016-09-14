package models

/**
  * Created by Sebastian on 2016-09-14.
  */
object JsonFormats {

  import play.api.libs.json.Json

  implicit val postFormat = Json.format[Post]
  implicit val commentFormat = Json.format[Comment]
}