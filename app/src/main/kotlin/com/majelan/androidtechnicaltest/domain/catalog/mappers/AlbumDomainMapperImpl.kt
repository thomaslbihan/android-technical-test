package com.majelan.androidtechnicaltest.domain.catalog.mappers

import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain
import javax.inject.Inject

class AlbumDomainMapperImpl @Inject constructor(
   private val mediaDomainMapper: MediaDomainMapper,
) : AlbumDomainMapper {
   override fun map(medias: List<Media>): AlbumDomain? {
      val first = medias.firstOrNull() ?: return null
      return AlbumDomain(
         name = first.album,
         picture = first.image,
         totalTrackCount = first.totalTrackCount,
         medias = medias.map(mediaDomainMapper::map),
      )
   }
}