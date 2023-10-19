package com.majelan.androidtechnicaltest.data.catalog.datasource

import com.majelan.androidtechnicaltest.data.catalog.api.CatalogApi
import com.majelan.androidtechnicaltest.data.catalog.entity.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import javax.inject.Inject

class RemoteCatalogDataSourceImpl @Inject constructor(
   private val api: CatalogApi,
) : CatalogDataSource {
   override suspend fun getMedias(): Either<List<Media>, AppException> {
      return api.getCatalog().mapSuccess { it.music }
   }
}