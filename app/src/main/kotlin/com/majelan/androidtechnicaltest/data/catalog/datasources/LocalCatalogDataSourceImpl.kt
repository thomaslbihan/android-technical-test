package com.majelan.androidtechnicaltest.data.catalog.datasources

import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import javax.inject.Inject

class LocalCatalogDataSourceImpl @Inject constructor() : LocalCatalogDataSource {
   private var medias: List<Media>? = null

   override suspend fun getMedias(): Either<List<Media>, AppException> {
      return medias?.let {
         Successful(it)
      } ?: run {
         Failure(AppException("Load media from a remote data source"))
      }
   }

   override fun update(medias: List<Media>) {
      this.medias = medias
   }
}