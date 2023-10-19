package com.majelan.androidtechnicaltest.presentation.artist_details.mappers

import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import javax.inject.Inject

class MediaUIMapperImpl @Inject constructor() : MediaUIMapper {
   override fun map(mediaDomain: MediaDomain): MediaUI {
      return MediaUI(
         id = mediaDomain.id,
         name = mediaDomain.name,
         trackNumber = mediaDomain.trackNumber,
         formattedTrackNumber = "${mediaDomain.trackNumber}."
      )
   }
}