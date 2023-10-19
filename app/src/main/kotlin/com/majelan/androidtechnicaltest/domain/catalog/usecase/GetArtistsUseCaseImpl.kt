package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.catalog.repository.CatalogRepository
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.di.DefaultDispatcher
import com.majelan.androidtechnicaltest.di.IODispatcher
import com.majelan.androidtechnicaltest.domain.catalog.entities.ArtistDomain
import com.majelan.androidtechnicaltest.domain.catalog.mappers.ArtistDomainMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class GetArtistsUseCaseImpl @Inject constructor(
   private val catalogRepository: CatalogRepository,
   private val artistMapper: ArtistDomainMapper,
   @IODispatcher private val ioDispatcher: CoroutineDispatcher,
   @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : GetArtistsUseCase {

   override suspend fun invoke(): Either<List<ArtistDomain>, AppException> {
      return ioDispatcher {
         catalogRepository.getMedias().mapSuccess { medias ->
            defaultDispatcher {
               medias
                  .groupBy { it.artist }
                  .mapNotNull { (_, medias) -> artistMapper.map(medias) }
            }
         }
      }
   }
}