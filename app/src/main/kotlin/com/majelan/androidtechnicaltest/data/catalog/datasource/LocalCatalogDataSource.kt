package com.majelan.androidtechnicaltest.data.catalog.datasource

import com.majelan.androidtechnicaltest.data.catalog.entity.Media

interface LocalCatalogDataSource: CatalogDataSource {
   fun update(medias: List<Media>)
}