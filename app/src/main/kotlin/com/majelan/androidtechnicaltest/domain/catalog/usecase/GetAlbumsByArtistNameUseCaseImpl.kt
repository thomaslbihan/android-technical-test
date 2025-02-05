package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.catalog.repositories.CatalogRepository
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.di.DefaultDispatcher
import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain
import com.majelan.androidtechnicaltest.domain.catalog.mappers.AlbumDomainMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class GetAlbumsByArtistNameUseCaseImpl @Inject constructor(
   private val catalogRepository: CatalogRepository,
   private val albumDomainMapper: AlbumDomainMapper,
   @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : GetAlbumsByArtistNameUseCase {

   override suspend fun invoke(name: String): Either<List<AlbumDomain>, AppException> {
      return catalogRepository.getMedias().mapSuccess { medias ->
         defaultDispatcher {
            medias.filter { it.artist == name }
               .groupBy { it.album }
               .mapNotNull { (_, medias) ->
                  albumDomainMapper.map(medias)
               }
         }
      }
   }
}