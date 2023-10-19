package com.majelan.androidtechnicaltest.data.catalog.repository

import com.majelan.androidtechnicaltest.data.catalog.datasource.CatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.datasource.LocalCatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.entity.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
   private val remoteDataSource: CatalogDataSource,
   private val localDataSource: LocalCatalogDataSource,
) : CatalogRepository {

   override suspend fun getMedias(): Either<List<Media>, AppException> {
      return when(val result = localDataSource.getMedias()) {
         is Successful -> result
         is Failure -> remoteDataSource.getMedias().onSuccess {
            localDataSource.update(it)
         }
      }
   }
}