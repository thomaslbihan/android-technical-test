package com.majelan.androidtechnicaltest.domain.catalog.mappers

import com.majelan.androidtechnicaltest.data.catalog.entity.Media
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain
import javax.inject.Inject

class MediaDomainMapperImpl @Inject constructor() : MediaDomainMapper {
   override fun map(media: Media): MediaDomain {
      return MediaDomain(
         id = media.id,
         picture = media.image,
         name = media.title,
         duration = media.duration,
         trackNumber = media.trackNumber,
         artist = media.artist,
         genre = media.genre,
      )
   }
}