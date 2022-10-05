package com.lunatech.goldenalgo.onboarding

import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.syntax._
import io.circe.generic.auto._
import akka.http.scaladsl.model.headers.RawHeader


class Controller()(implicit val ec: ExecutionContext) {

  // TODO: replace this hard-coded value by CRUD operations
  /*val recipe1 = Recipe("recipe1-id", "recipe1-name", Seq("ingredient1", "ingredient2"), Seq("instruction1", "instruction2"))

  val recipes = path("recipes") {
    get {
      respondWithHeader(RawHeader("Access-Control-Allow-Origin", "*")) {
        complete(HttpEntity(ContentTypes.`application/json`, Seq(recipe1).asJson.noSpaces))
      }
    }
  }*/


  val route = pathPrefix("api"){
    path("recipe"){
      get{
        complete(
          RecipeDao.getAllRecipe().map{returnVal =>
            HttpResponse(StatusCodes.OK, entity  = HttpEntity(ContentTypes.`application/json`,returnVal.asJson.toString ) )
          }
        )
      }~post{
        entity(as[RecipeInsertEntity]){ recipe =>
          complete(
            RecipeDao.insertRecipe(recipe) map { returnVal =>
              HttpResponse(StatusCodes.OK, entity = HttpEntity(returnVal))
            }
          )
        }
      }~put{
        entity(as[Recipe]){ recipe =>
          complete(
            RecipeDao.updateRecipe(recipe).map{ returnVal =>
              HttpResponse(StatusCodes.OK, entity = HttpEntity(returnVal))
            }
          )
        }
      }
    }

  }

  val routes: Route = route //recipes
}
