package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.catalog.repositories.CatalogRepository
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import com.majelan.androidtechnicaltest.di.DefaultDispatcher
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain
import com.majelan.androidtechnicaltest.domain.catalog.mappers.MediaDomainMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class GetMediaUseCaseImpl @Inject constructor(
   private val catalogRepository: CatalogRepository,
   private val mediaDomainMapper: MediaDomainMapper,
   @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : GetMediaUseCase {

   override suspend fun invoke(id: String): Either<MediaDomain, AppException> {
      return when (val result = catalogRepository.getMedias()) {
         is Successful -> {
            defaultDispatcher {
               result.data.firstOrNull { it.id == id }
                  ?.let { Successful(mediaDomainMapper.map(it)) }
                  ?: Failure(AppException("No media found with id=$id"))
            }
         }

         is Failure -> result
      }
   }
}