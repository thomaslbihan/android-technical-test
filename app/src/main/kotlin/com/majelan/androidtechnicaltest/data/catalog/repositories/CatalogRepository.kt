package com.majelan.androidtechnicaltest.data.catalog.repositories

import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either

interface CatalogRepository {

   suspend fun getMedias(): Either<List<Media>, AppException>
}