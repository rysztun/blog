package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import services.{PostService, SimpleUUIDGenerator}

import scala.concurrent.Future

/**
  * Created by Sebastian on 2016-09-14.
  */
class PostController @Inject()(val simpleUUIDGenerator: SimpleUUIDGenerator,
                               val commentController: CommentController, val postService: PostService)
                              extends Controller {

  import models.JsonFormats._
  import models._

  def create = Action.async(parse.json) { request =>
    val post = request.body.validate[Post]
    post.map { post =>
      postService.create(post)
        .map { lastError =>
          Logger.debug(s"Successfully inserted with LastError: $lastError")
          Created
        }
    }.getOrElse(Future.successful(BadRequest("Invalid json format")))

  }

  def find(uUID: String) = Action.async { request =>
    val futurePostList: Future[List[Post]] = postService.findByUUID(uUID)
    futurePostList.map { posts =>
      val post = posts.head
      Ok(Json.toJson(post))
    }
  }

  def findAll() = Action.async { request =>
    val futurePostList: Future[List[Post]] = postService.findAll
    futurePostList.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def update(uUID: String) = Action.async(parse.json) { request =>
    val post = request.body.validate[Post].getOrElse(Post.EmptyPost)

    val futureUpdate: Future[UpdateWriteResult] = postService.update(uUID, post)
    futureUpdate.map { result => Accepted }
  }

  def remove(uUID: String) = Action.async { request =>
    commentController.remove(uUID)
    val futureRemove: Future[WriteResult] = postService.remove(uUID)
    futureRemove.map(result => Accepted)
  }
}
