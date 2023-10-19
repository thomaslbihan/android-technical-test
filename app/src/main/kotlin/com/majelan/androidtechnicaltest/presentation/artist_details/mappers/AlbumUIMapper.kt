package com.majelan.androidtechnicaltest.presentation.artist_details.mappers

import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.AlbumUI

interface AlbumUIMapper {
   fun map(albumDomain: AlbumDomain): AlbumUI
}