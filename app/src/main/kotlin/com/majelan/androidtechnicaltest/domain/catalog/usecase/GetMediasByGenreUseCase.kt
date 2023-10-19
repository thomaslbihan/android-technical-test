package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain

interface GetMediasByGenreUseCase {

   suspend operator fun invoke(genreName: String): Either<List<MediaDomain>, AppException>
}