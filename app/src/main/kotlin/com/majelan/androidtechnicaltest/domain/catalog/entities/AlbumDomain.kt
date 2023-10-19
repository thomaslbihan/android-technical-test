package com.majelan.androidtechnicaltest.domain.catalog.entities

data class AlbumDomain(
   val name: String,
   val picture: String,
   val totalTrackCount: Int,
   val medias: List<MediaDomain>,
)
