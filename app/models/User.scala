package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class User(email: String, firstName: String, lastName: String, age: Int)

object User {



  implicit val reads: Reads[User] = (
    (__ \ "email").read[String](email) and
      (__ \ "firstName").read[String](minLength[String](2)) and
      (__ \ "lastName").read[String](minLength[String](2)) and
      (__ \ "age").read[Int]

    )(User.apply _)

  implicit val writes: Writes[User] = (
    (__ \ "email").write[String] and
      (__ \ "firstName").write[String] and
      (__ \ "lastName").write[String] and
      (__ \ "age").write[Int]
    )(unlift(User.unapply))

  private val userParser: RowParser[User] = {
    get[String]("email") ~
      get[String]("first_name") ~
      get[String]("last_name") ~
      get[Int]("age") map {
      case email ~ firstName ~ lastName ~ age => User(email, firstName, lastName, age)
    }
  }

  def findAll(): Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM users").as(User.userParser *)
    }
  }

  def getByEmail(email: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM users WHERE email = {email}").on('email -> email).as(User.userParser.singleOpt)
    }
  }

  def create(user: User): Unit = {
    DB.withConnection { implicit connection =>
      SQL("INSERT INTO users(email, first_name, last_name, age) VALUES ({email}, {first_name}, {last_name}, {age} )").on(
        'email -> user.email,
        'first_name -> user.firstName,
        'last_name -> user.lastName,
        'age -> user.age
      ).executeUpdate()
    }
  }

  def update(user: User): Unit = {
    DB.withConnection { implicit connection =>
      SQL("UPDATE users SET first_name = {first_name}, last_name={last_name}, age ={age} WHERE email = {email} ").on(
        'email -> user.email,
        'first_name -> user.firstName,
        'last_name -> user.lastName,
        'age -> user.age
      ).executeUpdate()
    }
  }

  def delete(email: String): Unit = {
    DB.withConnection { implicit connection =>
      SQL("DELETE FROM users WHERE email = {email}").on('email -> email).executeUpdate()
    }
  }
}
