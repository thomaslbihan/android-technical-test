package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.catalog.repositories.CatalogRepository
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.di.DefaultDispatcher
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain
import com.majelan.androidtechnicaltest.domain.catalog.mappers.MediaDomainMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class GetMediasByArtistUseCaseImpl @Inject constructor(
   private val catalogRepository: CatalogRepository,
   private val mediaDomainMapper: MediaDomainMapper,
   @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : GetMediasByArtistUseCase {
   override suspend fun invoke(artistName: String): Either<List<MediaDomain>, AppException> {
      return catalogRepository.getMedias().mapSuccess { medias ->
         defaultDispatcher {
            medias.filter { it.artist == artistName }
               .map(mediaDomainMapper::map)
         }
      }
   }
}