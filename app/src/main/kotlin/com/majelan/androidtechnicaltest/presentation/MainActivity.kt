package com.majelan.androidtechnicaltest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.majelan.androidtechnicaltest.presentation.media_player.PlayerManager
import com.majelan.androidtechnicaltest.presentation.navigation.Router
import com.majelan.androidtechnicaltest.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

   @Inject
   lateinit var playerManager: PlayerManager

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {
         AppTheme {
            Surface(
               modifier = Modifier.fillMaxSize(),
               color = MaterialTheme.colorScheme.background
            ) {
               Router()
            }
         }
      }
   }

   override fun onStart() {
      super.onStart()
      playerManager.onStart(this)
   }

   override fun onStop() {
      playerManager.onStop()
      super.onStop()
   }
}