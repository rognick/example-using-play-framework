package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index(name: String, age: Int) = Action {
    Created(views.html.index(name, age))
  }

}