package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import reactivemongo.api.commands.WriteResult
import services.{CommentService, SimpleUUIDGenerator}

import scala.concurrent.Future

/**
  * Created by Sebastian on 2016-09-14.
  */
class CommentController @Inject()(val simpleUUIDGenerator: SimpleUUIDGenerator,
                                  val commentService: CommentService) extends Controller {

  import models.JsonFormats._
  import models._

  def create = Action.async(parse.json) { request =>
    val comment = request.body.validate[Comment]
    comment.map { comment =>
      commentService.create(comment)
        .map { lastError =>
          Logger.debug(s"Successfully inserted with LastError: $lastError")
          Created
        }
    }.getOrElse(Future.successful(BadRequest("Invalid json format")))

  }

  def findAll(uUID: String) = Action.async { request =>

    val futurePostList: Future[List[Comment]] = commentService.findByUUID(uUID)
    futurePostList.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def remove(uUID: String) = {
    val futureRemove: Future[WriteResult] = commentService.remove(uUID)
    futureRemove.map(result => Accepted)
  }
}
