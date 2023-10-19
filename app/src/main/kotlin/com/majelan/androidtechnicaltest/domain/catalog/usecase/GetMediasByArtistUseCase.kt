package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain

interface GetMediasByArtistUseCase {

   suspend operator fun invoke(artistName: String): Either<List<MediaDomain>, AppException>
}