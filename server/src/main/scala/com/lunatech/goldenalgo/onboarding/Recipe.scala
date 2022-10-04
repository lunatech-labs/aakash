package com.lunatech.goldenalgo.onboarding

import com.sksamuel.elastic4s.{Hit, HitReader}
import io.circe._
import io.circe.generic.semiauto._
import io.circe.jawn.decode

import java.util.UUID


case class Recipe
(
  id: UUID,
  name: String,
  ingredients: Seq[String],
  instructions: Seq[String]
)

object Recipe {
  implicit val codec: Codec[Recipe] = deriveCodec[Recipe]

  implicit def recipeReader: HitReader[Recipe] =
    (hit: Hit) => decode[Recipe](hit.sourceAsString).toTry
}

case class RecipeInsertEntity
(
  name: String,
  ingredients: Seq[String],
  instructions: Seq[String]
)
object RecipeInsertEntity {
  implicit val codec: Codec[Recipe] = deriveCodec[Recipe]
}