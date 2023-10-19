package com.majelan.androidtechnicaltest.presentation.artist_list.mappers

import com.majelan.androidtechnicaltest.domain.catalog.entities.ArtistDomain
import com.majelan.androidtechnicaltest.presentation.artist_list.entities.ArtistUI

interface ArtistUIMapper {
   fun map(artist: ArtistDomain): ArtistUI
}