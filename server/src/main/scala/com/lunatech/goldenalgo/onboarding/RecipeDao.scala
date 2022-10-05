package com.lunatech.goldenalgo.onboarding

import com.lunatech.goldenalgo.onboarding.utils.DbSupport
import com.sksamuel.elastic4s.ElasticApi.indexExists
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.indexes.IndexResponse
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


import java.util.UUID

object RecipeDao {

  def checkDbIndex(index: String): Boolean = {
    DbSupport.client.execute {
      indexExists(index)
    }.await
      .result
      .isExists
  }

  def createDbIndex(index: String)= {

    DbSupport.client.execute {
      createIndex(index)
    }.await
  }

  def getAllRecipe()={

    DbSupport.client.execute {
      search("recipe")
    }.map{searchResponse =>

      searchResponse.result.hits.hits.map{searchHit =>
        println("response: " + searchHit)
        searchHit.to[Recipe] }
    }
  }


  def insertRecipe(recipe: RecipeInsertEntity)={

    DbSupport.client.execute{
      indexInto("recipe") doc (Recipe(UUID.randomUUID,recipe.name,recipe.ingredients,recipe.instructions ).asJson.noSpaces)
    }.map { response =>
      response.result.result match {
        case "created" => "Recipe Created."
        case "updated" => "Recipe Created."
        case _ => "Could not persist Recipe."
      }
    }
  }

  def updateRecipe(recipe: Recipe)={

    DbSupport.client.execute{
      updateByQuerySync("recipe", matchQuery("id",recipe.id) )
        .script(
          s"""
             |ctx._source.name = '${recipe.name}';
             |ctx._source.ingredients = ['${recipe.ingredients.mkString("','")}'];
             |ctx._source.instructions = ['${recipe.instructions.mkString("','")}'];
             |""".stripMargin
        )
    }.map{ response =>
      response.result.updated match {
        case 1 => "Recipe Updated."
        case _ => "Recipe Not Updated"
      }
    }
  }


}
