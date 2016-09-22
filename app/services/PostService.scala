package services

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

// BSON-JSON conversions/collection
import play.modules.reactivemongo.json.collection._
import reactivemongo.play.json._

/**
  * Created by Sebastian on 2016-09-22.
  */
class PostService @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                            val simpleUUIDGenerator: SimpleUUIDGenerator)
                            extends MongoController with ReactiveMongoComponents {

  def collection: JSONCollection = db.collection[JSONCollection]("blog")

  import models.JsonFormats._
  import models._

  def create(post: Post): Future[WriteResult] = {
    collection.insert(post.copy(uUID = simpleUUIDGenerator.generate.toString))
  }

  def findAll: Future[List[Post]] = {
    val cursor: Cursor[Post] = collection
      .find(Json.obj())
      .sort(Json.obj("created" -> -1))
      .cursor[Post]

    val futurePostList: Future[List[Post]] = cursor.collect[List]()
    futurePostList
  }

  def findByUUID(uUID: String): Future[List[Post]] = {
    val cursor: Cursor[Post] = collection
      .find(Json.obj(FieldNames.UUID -> uUID))
      .sort(Json.obj("created" -> -1))
      .cursor[Post]

    val futurePostList: Future[List[Post]] = cursor.collect[List]()
    futurePostList
  }

  def update(uUID: String, post: Post): Future[UpdateWriteResult] = {
    val modifier = BSONDocument("$set" -> BSONDocument(
      FieldNames.Title -> post.title,
      FieldNames.ShortDesc -> post.shortDesc,
      FieldNames.Content -> post.content,
      FieldNames.UUID -> post.uUID,
      FieldNames.Keywords -> post.keywords,
      FieldNames.Date -> post.date
    ))

    val futureUpdate = collection.update(Json.obj(FieldNames.UUID -> uUID), post)
    futureUpdate
  }

  def remove(uUID: String): Future[WriteResult] = {
    val futureRemove = collection.remove(Json.obj(FieldNames.UUID -> uUID))
    futureRemove
  }

}
