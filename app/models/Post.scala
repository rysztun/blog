package models

/**
  * Created by Sebastian on 2016-09-13.
  */
case class Post(title: String, shortDesc: String, content: String, uUID: String, keywords: String, date: Long)

object Post {
  val EmptyPost = new Post("", "","","","", new java.util.Date().getTime)
}



