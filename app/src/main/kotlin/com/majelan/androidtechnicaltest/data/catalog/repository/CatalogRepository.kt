package com.majelan.androidtechnicaltest.data.catalog.repository

import com.majelan.androidtechnicaltest.data.catalog.entity.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either

interface CatalogRepository {

   suspend fun getMedias(): Either<List<Media>, AppException>
}