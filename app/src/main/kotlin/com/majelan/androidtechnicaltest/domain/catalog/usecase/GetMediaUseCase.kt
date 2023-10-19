package com.majelan.androidtechnicaltest.domain.catalog.usecase

import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain

interface GetMediaUseCase {

   suspend operator fun invoke(id: String): Either<MediaDomain, AppException>
}