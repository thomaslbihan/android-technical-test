package com.majelan.androidtechnicaltest.presentation.media_player

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {
   private var mediaSession: MediaSession? = null

   override fun onGetSession(
      controllerInfo: MediaSession.ControllerInfo
   ): MediaSession? = mediaSession

   override fun onCreate() {
      super.onCreate()
      val player = ExoPlayer.Builder(this).build().apply { playWhenReady = true }
      mediaSession = MediaSession.Builder(this, player)
         .build()
   }

   override fun onTaskRemoved(rootIntent: Intent?) {
      stopSelf()
      super.onTaskRemoved(rootIntent)
   }

   override fun onDestroy() {
      mediaSession?.run {
         player.release()
         release()
         mediaSession = null
      }
      super.onDestroy()
   }
}