package controllers

import models.User
import models.User._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsError, Json}
import play.api.mvc._

object UserController extends Controller {


  val userForm = Form(
    mapping(
      "email" -> email,
      "firstName" -> text(minLength = 2),
      "lastName" -> text(minLength = 2),
      "age" -> number
    )(User.apply)(User.unapply)
  )

  def create = Action(parse.json) { request =>
    request.body.validate[User].map {
      case user =>
        User.create(user)
        Created(Json.toJson(user))
    }.recoverTotal {
      e => BadRequest(JsError.toFlatJson(e))
    }
  }

  def all = Action {
    Ok(Json.toJson(User.findAll()))
  }

  def getByEmail(email: String) = Action {
    Ok(Json.toJson(User.getByEmail(email)))
  }

  def update = Action(parse.json) {
    request =>
      request.body.validate[User].map {
        case user =>
          User.update(user)
          Ok(Json.toJson(user))
      }.recoverTotal {
        e => BadRequest(JsError.toFlatJson(e))
      }
  }

  def viewUpdateUser(email: String) = Action {
    var user = User.getByEmail(email)
    Ok(views.html.editUser(user.get, userForm))
  }

  def updateUser = Action { implicit request =>
    userForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(views.html.form(User.findAll(), formWithErrors))
      },
      user => {
        User.update(user)
        Redirect(routes.UserController.viewUpdateUser(user.email))
      }
    )
  }


  def delete(email: String) = Action {
    User.delete(email)
    Redirect(routes.UserController.showUserForm())
  }

  def showUserForm = Action {
    Ok(views.html.form(User.findAll(), userForm))
  }

  def submit = Action { implicit request =>
    userForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(views.html.form(User.findAll(), formWithErrors))
      },
      user => {
        User.create(user)
        Redirect(routes.UserController.showUserForm())
      }
    )
  }

}
