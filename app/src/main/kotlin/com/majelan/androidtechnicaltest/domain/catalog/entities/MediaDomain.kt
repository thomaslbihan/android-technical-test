package com.majelan.androidtechnicaltest.domain.catalog.entities

data class MediaDomain(
   val id: String,
   val picture: String,
   val name: String,
   val songUri: String,
   val duration: Int,
   val trackNumber: Int,
   val artist: String,
   val genre: String,
)