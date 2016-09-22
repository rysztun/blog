package services

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

// BSON-JSON conversions/collection
import play.modules.reactivemongo.json.collection._
import reactivemongo.play.json._

/**
  * Created by Sebastian on 2016-09-22.
  */
class CommentService @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends MongoController with ReactiveMongoComponents {
  def collection: JSONCollection = db.collection[JSONCollection]("comments")

  import models.JsonFormats._
  import models._

  def create(comment: Comment): Future[WriteResult] = {
    collection.insert(comment)
  }

  def findByUUID(uUID: String): Future[List[Comment]] = {
    val cursor: Cursor[Comment] = collection
      .find(Json.obj(FieldNames.UUID -> uUID))
      .sort(Json.obj("created" -> -1))
      .cursor[Comment]
    //cursor
    val futurePostList: Future[List[Comment]] = cursor.collect[List]()
    futurePostList
  }

  def remove(uUID: String): Future[WriteResult] = {
    val futureRemove = collection.remove(Json.obj(FieldNames.UUID -> uUID))
    futureRemove
  }
}
