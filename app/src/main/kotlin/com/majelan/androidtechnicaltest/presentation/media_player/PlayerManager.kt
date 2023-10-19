package com.majelan.androidtechnicaltest.presentation.media_player

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.annotation.UiThread
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerManager @Inject constructor() {
   private lateinit var sessionToken: SessionToken
   private lateinit var controllerFuture: ListenableFuture<MediaController>
   private val controller = MutableStateFlow<MediaController?>(null)

   private val _isPlayingFlow = MutableStateFlow(false)
   val isPlayingFlow: StateFlow<Boolean> = _isPlayingFlow

   private suspend fun getController() = controller.filterNotNull().first()

   fun onStart(context: Context) {
      sessionToken = SessionToken(
         context,
         ComponentName(context, PlaybackService::class.java)
      )
      controllerFuture = MediaController
         .Builder(context, sessionToken)
         .buildAsync()

      controllerFuture.addListener({
         controllerFuture.get().apply {
            controller.update { this }
            addListener(object : Player.Listener {
               override fun onIsPlayingChanged(isPlaying: Boolean) {
                  super.onIsPlayingChanged(isPlaying)
                  Log.d("TLB", "isPlaying $isPlaying")
                  _isPlayingFlow.update { isPlaying }
               }
            })
         }
      }, MoreExecutors.directExecutor())
   }

   fun onStop() {
      MediaController.releaseFuture(controllerFuture)
   }

   @UiThread
   suspend fun setMedia(uri: String) {
      getController().setMediaItem(MediaItem.fromUri(uri))
   }

   suspend fun play() {
      getController().play()
   }

   suspend fun pause() {
      getController().pause()
   }
}