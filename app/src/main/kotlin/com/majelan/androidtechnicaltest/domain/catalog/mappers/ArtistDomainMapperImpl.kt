package com.majelan.androidtechnicaltest.domain.catalog.mappers

import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.domain.catalog.entities.ArtistDomain
import javax.inject.Inject

class ArtistDomainMapperImpl @Inject constructor() : ArtistDomainMapper {

   override fun map(medias: List<Media>): ArtistDomain? {
      val first = medias.firstOrNull() ?: return null
      return ArtistDomain(
         name = first.artist,
         picture = first.image,
         albumCount = medias.distinctBy { it.album }.size
      )
   }
}