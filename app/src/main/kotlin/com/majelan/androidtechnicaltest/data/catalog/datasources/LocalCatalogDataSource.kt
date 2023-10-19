package com.majelan.androidtechnicaltest.data.catalog.datasources

import com.majelan.androidtechnicaltest.data.catalog.entities.Media

interface LocalCatalogDataSource: CatalogDataSource {
   fun update(medias: List<Media>)
}