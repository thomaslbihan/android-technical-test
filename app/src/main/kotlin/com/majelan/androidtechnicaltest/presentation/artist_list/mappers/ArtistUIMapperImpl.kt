package com.majelan.androidtechnicaltest.presentation.artist_list.mappers

import com.majelan.androidtechnicaltest.domain.catalog.entities.ArtistDomain
import com.majelan.androidtechnicaltest.presentation.artist_list.entities.ArtistUI
import javax.inject.Inject

class ArtistUIMapperImpl @Inject constructor() : ArtistUIMapper {
   override fun map(artist: ArtistDomain): ArtistUI {
      return ArtistUI(
         name = artist.name,
         picture = artist.picture,
         albumCount = artist.albumCount
      )
   }
}