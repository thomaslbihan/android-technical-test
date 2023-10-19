package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain

interface GetAlbumsByArtistNameUseCase {

   suspend operator fun invoke(name: String): Either<List<AlbumDomain>, AppException>
}