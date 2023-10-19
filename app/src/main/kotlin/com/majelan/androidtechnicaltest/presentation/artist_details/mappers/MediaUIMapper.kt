package com.majelan.androidtechnicaltest.presentation.artist_details.mappers

import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI

interface MediaUIMapper {
   fun map(mediaDomain: MediaDomain): MediaUI
}
