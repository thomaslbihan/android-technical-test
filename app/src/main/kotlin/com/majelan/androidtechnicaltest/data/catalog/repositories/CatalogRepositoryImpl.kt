package com.majelan.androidtechnicaltest.data.catalog.repositories

import com.majelan.androidtechnicaltest.data.catalog.datasources.CatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.datasources.LocalCatalogDataSource
import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import com.majelan.androidtechnicaltest.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
   private val remoteDataSource: CatalogDataSource,
   private val localDataSource: LocalCatalogDataSource,
   @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : CatalogRepository {

   override suspend fun getMedias(): Either<List<Media>, AppException> {
      return ioDispatcher {
         when(val result = localDataSource.getMedias()) {
            is Successful -> result
            is Failure -> remoteDataSource.getMedias().onSuccess {
               localDataSource.update(it)
            }
         }
      }
   }
}