package com.majelan.androidtechnicaltest.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavType.Companion
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsScreen
import com.majelan.androidtechnicaltest.presentation.artist_list.ArtistListScreen
import com.majelan.androidtechnicaltest.presentation.navigation.Destination.ArtistDetails
import com.majelan.androidtechnicaltest.presentation.navigation.Destination.ArtistList
import com.majelan.androidtechnicaltest.presentation.navigation.Destination.Player
import com.majelan.androidtechnicaltest.presentation.player.PlayerScreen

@Composable
fun Router() {
   val navController = rememberNavController()

   NavHost(
      navController = navController,
      startDestination = Destination.ArtistList.name,
      enterTransition = {
         slideIntoContainer(towards = SlideDirection.Start)
      },
      exitTransition = {
         slideOutOfContainer(towards = SlideDirection.Start)
      },
      popEnterTransition = {
         slideIntoContainer(towards = SlideDirection.End)
      },
      popExitTransition = {
         slideOutOfContainer(towards = SlideDirection.End)
      },
   ) {
      composable(ArtistList.name) { ArtistListScreen(navController = navController) }

      composable(
         route = ArtistDetails().name,
         arguments = listOf(
            navArgument(ArtistDetails.Argument.ARTIST_NAME.value) {
               type = NavType.StringType
            }
         )
      ) { ArtistDetailsScreen(navController = navController) }

      composable(
         route = Player().name,
         arguments = listOf(
            navArgument(Player.Argument.MEDIA_ID.value) {
               type = NavType.StringType
            }
         )
      ) { PlayerScreen(navController = navController) }
   }
}