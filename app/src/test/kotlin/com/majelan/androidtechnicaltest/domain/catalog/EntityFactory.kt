package com.majelan.androidtechnicaltest.domain.catalog

import com.majelan.androidtechnicaltest.data.catalog.entities.Media
import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain

object EntityFactory {

   fun getEmptyMedia() = Media(
      id = "",
      title = "",
      album = "",
      genre = "",
      artist = "",
      source = "",
      image = "",
      trackNumber = 0,
      totalTrackCount = 0,
      duration = 0,
      site = ""
   )

   fun getEmptyAlbumDomain() = AlbumDomain(
      name = "",
      picture = "",
      totalTrackCount = 0,
      medias = emptyList()
   )

   fun getEmptyMediaDomain() = MediaDomain(
      id = "",
      picture = "",
      name = "",
      songUri = "",
      duration = 0,
      trackNumber = 0,
      artist = "",
      genre = ""
   )
}