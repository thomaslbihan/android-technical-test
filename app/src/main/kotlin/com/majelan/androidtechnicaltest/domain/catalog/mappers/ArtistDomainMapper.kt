package com.majelan.androidtechnicaltest.domain.catalog.mappers

import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.domain.catalog.entities.ArtistDomain

interface ArtistDomainMapper {

   fun map(medias: List<Media>): ArtistDomain?
}