package com.majelan.androidtechnicaltest.presentation.artist_details.mappers

import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.AlbumUI
import javax.inject.Inject

class AlbumUIMapperImpl @Inject constructor(
   private val mediaUIMapper: MediaUIMapper
) : AlbumUIMapper {
   override fun map(albumDomain: AlbumDomain): AlbumUI {
      val totalDuration = albumDomain.medias.sumOf { it.duration }
      val hours = (totalDuration / (60 * 60))
      val minutes = (totalDuration % (60 * 60)) / 60
      val seconds = (totalDuration % (60 * 60) % (60))

      val formattedDuration = "${hours}h ${minutes}mn ${seconds}s"

      return AlbumUI(
         name = albumDomain.name,
         picture = albumDomain.picture,
         totalTrackCount = albumDomain.totalTrackCount,
         formattedTotalDuration = formattedDuration,
         medias = albumDomain.medias
            .map(mediaUIMapper::map)
            .sortedBy { it.trackNumber }
      )
   }
}