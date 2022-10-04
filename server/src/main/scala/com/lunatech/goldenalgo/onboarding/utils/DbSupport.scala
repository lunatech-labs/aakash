package com.lunatech.goldenalgo.onboarding.utils

import com.lunatech.goldenalgo.onboarding.RecipeDao
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}

object DbSupport {

  val props = ElasticProperties("http://0.0.0.0:9200")
  val client = ElasticClient(JavaClient(props))

  def createDatabase(name:String)={
    if(!RecipeDao.checkDbIndex(name)){
      RecipeDao.createDbIndex(name)
    }
  }

}
