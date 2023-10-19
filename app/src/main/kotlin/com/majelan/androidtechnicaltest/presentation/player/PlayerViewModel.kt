package com.majelan.androidtechnicaltest.presentation.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import com.majelan.androidtechnicaltest.di.DefaultDispatcher
import com.majelan.androidtechnicaltest.di.IODispatcher
import com.majelan.androidtechnicaltest.domain.catalog.entities.MediaDomain
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediaUseCase
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediasByArtistUseCase
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetMediasByGenreUseCase
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import com.majelan.androidtechnicaltest.presentation.artist_details.mappers.MediaUIMapper
import com.majelan.androidtechnicaltest.presentation.media_player.PlayerManager
import com.majelan.androidtechnicaltest.presentation.mvi.MVIViewModel
import com.majelan.androidtechnicaltest.presentation.navigation.Destination.Player.Argument
import com.majelan.androidtechnicaltest.presentation.player.PlayerAction.NavigateBack
import com.majelan.androidtechnicaltest.presentation.player.PlayerAction.ScrollToTop
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnArtistTracksRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnBackButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnMediaItemClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnPauseButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnPlayButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnRecommendationsRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnRetryMediaButtonClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
   savedStateHandle: SavedStateHandle,
   private val getMediaUseCase: GetMediaUseCase,
   private val getMediasByArtistUseCase: GetMediasByArtistUseCase,
   private val getMediasByGenreUseCase: GetMediasByGenreUseCase,
   private val mediaUIMapper: MediaUIMapper,
   private val playerManager: PlayerManager,
   @IODispatcher private val ioDispatcher: CoroutineDispatcher,
   @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
): MVIViewModel<PlayerState, PlayerEvent, PlayerAction>(
   initialState = PlayerState(
      mediaId = savedStateHandle.get<String>(Argument.MEDIA_ID.value).orEmpty(),
      isMediaLoadingVisible = false,
      isMediaErrorVisible = false,
      picture = "",
      title = "",
      artistName = "",
      artistTracks = emptyList(),
      isArtistTracksLoadingVisible = false,
      isArtistTracksListVisible = false,
      isArtistTracksEmptyStateVisible = false,
      isArtistTracksErrorVisible = false,
      genre = "",
      recommendations = emptyList(),
      isRecommendationsLoadingVisible = false,
      isRecommendationsListVisible = false,
      isRecommendationsEmptyStateVisible = false,
      isRecommendationsErrorVisible = false,
      isPlaying = false,
   )
) {

   init {
      observeMediaId()
      observeArtistName()
      observeGenre()
      observePlayerManager()
   }

   override fun process(event: PlayerEvent) {
      when(event) {
         OnBackButtonClicked -> onBackButtonClicked()
         OnPauseButtonClicked -> onPauseButtonClicked()
         OnPlayButtonClicked -> onPlayButtonClicked()
         OnRetryMediaButtonClicked -> onRetryMediaButtonClicked()
         OnArtistTracksRetryButtonClicked -> onArtistTracksRetryButtonClicked()
         is OnMediaItemClicked -> onMediaItemClicked(event.media)
         OnRecommendationsRetryButtonClicked -> onRecommendationsRetryButtonClicked()
      }
   }

   private fun onBackButtonClicked() {
      emit(NavigateBack)
   }

   private fun onPauseButtonClicked() {
      viewModelScope.launch {
         playerManager.pause()
      }
   }

   private fun onPlayButtonClicked() {
      viewModelScope.launch {
         playerManager.play()
      }
   }

   private fun onRetryMediaButtonClicked() {
      fetchMedia(state.mediaId)
   }

   private fun onArtistTracksRetryButtonClicked() {
      fetchMediasByArtist(state.artistName)
   }

   private fun onRecommendationsRetryButtonClicked() {
      fetchMediasByGenre(state.genre)
   }

   private fun onMediaItemClicked(media: MediaUI) {
      emit(ScrollToTop)
      fetchMedia(media.id)
   }

   //region Fetch media
   private fun observeMediaId() {
      viewModelScope.launch(defaultDispatcher) {
         stateFlow.map { it.mediaId }
            .distinctUntilChanged()
            .collect { mediaId ->
               fetchMedia(mediaId)
            }
      }
   }

   private fun fetchMedia(mediaId: String) {
      viewModelScope.launch(ioDispatcher) {
         updateState {
            it.copy(
               isMediaLoadingVisible = true,
               isMediaErrorVisible = false,
            )
         }
         when(val result = getMediaUseCase(mediaId)) {
            is Successful -> handleFetchMediaSuccess(result.data)
            is Failure -> handleFetchMediaFailure()
         }
      }
   }

   private fun handleFetchMediaSuccess(media: MediaDomain) {
      updateState {
         it.copy(
            picture = media.picture,
            title = media.name,
            artistName = media.artist,
            genre = media.genre,
            isMediaLoadingVisible = false,
         )
      }
      viewModelScope.launch {
         playerManager.setMedia(media.songUri)
         playerManager.play()
      }
   }

   private fun handleFetchMediaFailure() {
      updateState {
         it.copy(
            isMediaLoadingVisible = false,
            isMediaErrorVisible = true
         )
      }
   }
   //endregion

   //region Fetch medias by artist
   private fun observeArtistName() {
      viewModelScope.launch(defaultDispatcher) {
         stateFlow.mapNotNull { state ->
            state.artistName.takeIf { it.isNotBlank() }
         }
            .distinctUntilChanged()
            .collect { artistName ->
               fetchMediasByArtist(artistName)
            }
      }
   }

   private fun fetchMediasByArtist(artistName: String) {
      viewModelScope.launch(ioDispatcher) {
         updateState {
            it.copy(
               isArtistTracksErrorVisible = false,
               isArtistTracksLoadingVisible = true,
            )
         }
         when (val result = getMediasByArtistUseCase(artistName)) {
            is Successful -> handleFetchMediasByArtistSuccess(result.data)
            is Failure -> handleFetchMediasByArtistFailure()
         }
      }
   }

   private suspend fun handleFetchMediasByArtistSuccess(medias: List<MediaDomain>) {
      defaultDispatcher {
         val isEmpty = medias.isEmpty()
         updateState {
            it.copy(
               artistTracks = medias.map(mediaUIMapper::map),
               isArtistTracksListVisible = !isEmpty,
               isArtistTracksEmptyStateVisible = isEmpty,
               isArtistTracksLoadingVisible = false
            )
         }
      }
   }

   private fun handleFetchMediasByArtistFailure() {
      updateState {
         it.copy(
            isArtistTracksErrorVisible = true,
            artistTracks = emptyList(),
            isArtistTracksEmptyStateVisible = false,
            isArtistTracksLoadingVisible = false,
            isArtistTracksListVisible = false,
         )
      }
   }
   //endregion

   //region Fetch medias by genre
   private fun observeGenre() {
      viewModelScope.launch(defaultDispatcher) {
         stateFlow.mapNotNull { state ->
            state.genre.takeIf { it.isNotBlank() }
         }
            .distinctUntilChanged()
            .collect { genre ->
               fetchMediasByGenre(genre)
            }
      }
   }

   private fun fetchMediasByGenre(genre: String) {
      viewModelScope.launch(ioDispatcher) {updateState {
         it.copy(
            isRecommendationsErrorVisible = false,
            isRecommendationsLoadingVisible = true,
         )
      }
         when(val result = getMediasByGenreUseCase(genre)) {
            is Successful -> handleFetchMediasByGenreSuccess(result.data)
            is Failure -> handleFetchMediasByGenreFailure()
         }
      }
   }

   private suspend fun handleFetchMediasByGenreSuccess(medias: List<MediaDomain>) {
      defaultDispatcher {
         val isEmpty = medias.isEmpty()
         updateState {
            it.copy(
               recommendations = medias.map(mediaUIMapper::map),
               isRecommendationsListVisible = !isEmpty,
               isRecommendationsEmptyStateVisible = isEmpty,
               isRecommendationsLoadingVisible = false
            )
         }
      }
   }

   private fun handleFetchMediasByGenreFailure() {
      updateState {
         it.copy(
            isRecommendationsErrorVisible = true,
            recommendations = emptyList(),
            isRecommendationsEmptyStateVisible = false,
            isRecommendationsLoadingVisible = false,
            isRecommendationsListVisible = false,
         )
      }
   }
   //endregion

   private fun observePlayerManager() {
      viewModelScope.launch(ioDispatcher) {
         playerManager.isPlayingFlow.collect { isPlaying ->
            updateState { it.copy(isPlaying = isPlaying) }
         }
      }
   }
}