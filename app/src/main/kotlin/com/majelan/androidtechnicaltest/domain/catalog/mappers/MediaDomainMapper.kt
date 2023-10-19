package com.majelan.androidtechnicaltest.domain.catalog.mappers

import com.majelan.androidtechnicaltest.data.catalog.entity.Media
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain

interface MediaDomainMapper {

   fun map(media: Media): MediaDomain
}