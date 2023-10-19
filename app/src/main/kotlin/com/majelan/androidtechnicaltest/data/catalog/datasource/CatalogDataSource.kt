package com.majelan.androidtechnicaltest.data.catalog.datasource

import com.majelan.androidtechnicaltest.data.catalog.entity.Media
import com.majelan.androidtechnicaltest.data.core.AppException
import com.majelan.androidtechnicaltest.data.core.either.Either

interface CatalogDataSource {

   suspend fun getMedias(): Either<List<Media>, AppException>
}