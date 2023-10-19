package com.majelan.androidtechnicaltest.presentation.artist_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import com.majelan.androidtechnicaltest.di.DefaultDispatcher
import com.majelan.androidtechnicaltest.di.IODispatcher
import com.majelan.androidtechnicaltest.domain.catalog.entities.AlbumDomain
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetAlbumsByArtistNameUseCase
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsAction.NavigateBack
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsAction.NavigateToPlayer
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnBackButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnMediaItemClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnPlayAlbumButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.AlbumUI
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import com.majelan.androidtechnicaltest.presentation.artist_details.mappers.AlbumUIMapper
import com.majelan.androidtechnicaltest.presentation.mvi.MVIViewModel
import com.majelan.androidtechnicaltest.presentation.navigation.Destination.ArtistDetails.Argument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
   savedStateHandle: SavedStateHandle,
   private val getAlbumsByArtistNameUseCase: GetAlbumsByArtistNameUseCase,
   private val albumUIMapper: AlbumUIMapper,
   @IODispatcher private val ioDispatcher: CoroutineDispatcher,
   @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
): MVIViewModel<ArtistDetailsState, ArtistDetailsEvent, ArtistDetailsAction>(
   initialState = ArtistDetailsState(
      name = "",
      albums = emptyList(),
      isListVisible = false,
      isLoadingVisible = false,
      isErrorVisible = false,
      isEmptyStateVisible = false,
   )
) {
   private val artistName = savedStateHandle.get<String>(Argument.ARTIST_NAME.value).orEmpty()

   init {
      fetchAlbums()
   }

   override fun process(event: ArtistDetailsEvent) {
      when(event) {
         OnBackButtonClicked -> onBackButtonClicked()
         is OnMediaItemClicked -> onMediaItemClicked(event.media)
         is OnPlayAlbumButtonClicked -> onPlayAlbumButtonClicked(event.album)
         OnRetryButtonClicked -> onRetryButtonClicked()
      }
   }

   private fun onBackButtonClicked() {
      emit(NavigateBack)
   }

   private fun onMediaItemClicked(media: MediaUI) {
      emit(NavigateToPlayer(media.id))
   }

   private fun onPlayAlbumButtonClicked(album: AlbumUI) {
      val first = album.medias.firstOrNull() ?: return
      emit(NavigateToPlayer(first.id))
   }

   private fun onRetryButtonClicked() {
      fetchAlbums()
   }

   private fun fetchAlbums() {
      viewModelScope.launch(ioDispatcher) {
         updateState {
            it.copy(
               isLoadingVisible = true,
               isErrorVisible = false,
            )
         }
         when(val result = getAlbumsByArtistNameUseCase(artistName)) {
            is Successful -> handleFetchAlbumsSuccess(result.data)
            is Failure -> handleFetchAlbumsFailure()
         }
      }
   }

   private suspend fun handleFetchAlbumsSuccess(albums: List<AlbumDomain>) {
      defaultDispatcher {
         val isEmpty = albums.isEmpty()
         updateState {
            it.copy(
               name = artistName,
               albums = albums.map(albumUIMapper::map),
               isListVisible = !isEmpty,
               isEmptyStateVisible = isEmpty,
               isLoadingVisible = false,
            )
         }
      }
   }

   private fun handleFetchAlbumsFailure() {
      updateState {
         it.copy(
            isErrorVisible = true,
            isListVisible = false,
            isEmptyStateVisible = false,
         )
      }
   }
}