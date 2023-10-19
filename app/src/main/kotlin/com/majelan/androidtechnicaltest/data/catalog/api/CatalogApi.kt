package com.majelan.androidtechnicaltest.data.catalog.api

import com.majelan.androidtechnicaltest.data.catalog.entities.CatalogReponse
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import retrofit2.http.GET

interface CatalogApi {

   @GET("catalog.json")
   suspend fun getCatalog(): Either<CatalogReponse, AppException>
}