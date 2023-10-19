package com.majelan.androidtechnicaltest.domain.catalog.mappers

import com.majelan.androidtechnicaltest.data.catalog.entity.Media
import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain

interface AlbumDomainMapper {
   fun map(medias: List<Media>): AlbumDomain?
}