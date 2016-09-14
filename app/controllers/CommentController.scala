package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import reactivemongo.bson.BSONDocument
import services.SimpleUUIDGenerator

import scala.concurrent.Future

// Reactive Mongo imports
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor

// BSON-JSON conversions/collection
import play.modules.reactivemongo.json.collection._
import reactivemongo.play.json._

/**
  * Created by Sebastian on 2016-09-14.
  */
class CommentController @Inject()(val reactiveMongoApi: ReactiveMongoApi, val simpleUUIDGenerator: SimpleUUIDGenerator)
  extends Controller with MongoController with ReactiveMongoComponents {

  def collection: JSONCollection = db.collection[JSONCollection]("comments")

  import models.JsonFormats._
  import models._

  def create = Action.async(parse.json) { request =>
    val comment = request.body.validate[Comment]
    comment.map { comment =>
      collection.insert(Comment(comment.uUID, comment.author, comment.comment))
        .map { lastError =>
          Logger.debug(s"Successfully inserted with LastError: $lastError")
          Created
        }
    }.getOrElse(Future.successful(BadRequest("Invalid json format")))

  }

  def findAll(uUID: String) = Action.async { request =>
    val cursor: Cursor[Comment] = collection.
      find(Json.obj(FieldNames.uUID -> uUID)).
      sort(Json.obj("created" -> -1)).
      cursor[Comment]

    val futurePostList: Future[List[Comment]] = cursor.collect[List]()
    futurePostList.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def remove(uUID: String) = {
    val futureRemove = collection.remove(Json.obj(FieldNames.uUID -> uUID))
    futureRemove.map(result => Accepted)
  }
}
