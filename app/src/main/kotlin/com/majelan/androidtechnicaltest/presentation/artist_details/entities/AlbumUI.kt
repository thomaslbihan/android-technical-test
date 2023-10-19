package com.majelan.androidtechnicaltest.presentation.artist_details.entities

data class AlbumUI(
   val name: String,
   val picture: String,
   val totalTrackCount: Int,
   val formattedTotalDuration: String,
   val medias: List<MediaUI>
)
