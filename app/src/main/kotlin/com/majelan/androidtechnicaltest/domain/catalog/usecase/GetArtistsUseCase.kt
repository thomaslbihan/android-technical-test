package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.domain.catalog.entities.ArtistDomain

interface GetArtistsUseCase {

   suspend operator fun invoke(): Either<List<ArtistDomain>, AppException>
}