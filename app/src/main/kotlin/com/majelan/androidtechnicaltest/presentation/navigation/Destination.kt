package com.majelan.androidtechnicaltest.presentation.navigation

sealed class Destination(val name: String, val route: String) {

   object ArtistList: Destination(name = "artist_list", route = "artist_list")

   class ArtistDetails(
      artistName: String = "",
   ): Destination(
      name = "artist_details/{${Argument.ARTIST_NAME.value}}",
      route = "artist_details/$artistName"
   ) {
      enum class Argument(val value: String) {
         ARTIST_NAME("artist_name")
      }
   }

   class Player(
      mediaId: String = ""
   ): Destination(
      name = "player/{${Argument.MEDIA_ID.value}}",
      route = "player/$mediaId"
   ) {
      enum class Argument(val value: String) {
         MEDIA_ID("media_id")
      }
   }
}