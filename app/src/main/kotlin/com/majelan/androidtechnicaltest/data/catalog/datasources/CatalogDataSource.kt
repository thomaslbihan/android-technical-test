package com.majelan.androidtechnicaltest.data.catalog.datasources

import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either

interface CatalogDataSource {

   suspend fun getMedias(): Either<List<Media>, AppException>
}