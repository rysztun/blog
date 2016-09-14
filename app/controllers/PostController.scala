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
class PostController @Inject()(val reactiveMongoApi: ReactiveMongoApi, val simpleUUIDGenerator: SimpleUUIDGenerator, commentController: CommentController)
  extends Controller with MongoController with ReactiveMongoComponents {

  def collection: JSONCollection = db.collection[JSONCollection]("blog")

  import models.JsonFormats._
  import models._

  def create = Action.async(parse.json) { request =>
    val post = request.body.validate[Post]
    post.map { post =>
      collection.insert(Post(post.title, post.shortDesc, post.content, simpleUUIDGenerator.generate.toString, post.keywords, post.date))
        .map { lastError =>
          Logger.debug(s"Successfully inserted with LastError: $lastError")
          Created
        }
    }.getOrElse(Future.successful(BadRequest("Invalid json format")))

  }

  def find(uUID: String) = Action.async { request =>
    val cursor: Cursor[Post] = collection.
      find(Json.obj(FieldNames.uUID -> uUID)).
      sort(Json.obj("created" -> -1)).
      cursor[Post]

    val futurePostList: Future[List[Post]] = cursor.collect[List]()
    futurePostList.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def findAll() = Action.async { request =>
    val cursor: Cursor[Post] = collection.
      find(Json.obj()).
      sort(Json.obj("created" -> -1)).
      cursor[Post]

    val futurePostList: Future[List[Post]] = cursor.collect[List]()
    futurePostList.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def remove(uUID: String) = Action.async { request =>
    commentController.remove(uUID)
    val futureRemove = collection.remove(Json.obj(FieldNames.uUID -> uUID))
    futureRemove.map(result => Accepted)
  }

  def update(uUID: String) = Action.async(parse.json) { request =>
    val post = request.body.validate[Post].getOrElse(Post("", "", "", "", "", 0))
    val modifier = BSONDocument("$set" -> BSONDocument(
      FieldNames.title -> post.title,
      FieldNames.shortDesc -> post.shortDesc,
      FieldNames.content -> post.content,
      FieldNames.uUID -> post.uUID,
      FieldNames.keywords -> post.keywords,
      FieldNames.date -> post.date
    ))

    val futureUpdate = collection.update(Json.obj(FieldNames.uUID -> uUID), post)
    futureUpdate.map { result => Accepted }
  }
}
