package com.majelan.androidtechnicaltest.presentation.artist_list

import androidx.lifecycle.viewModelScope
import com.majelan.androidtechnicaltest.data.core.either.Either.Failure
import com.majelan.androidtechnicaltest.data.core.either.Either.Successful
import com.majelan.androidtechnicaltest.di.DefaultDispatcher
import com.majelan.androidtechnicaltest.di.IODispatcher
import com.majelan.androidtechnicaltest.domain.catalog.entities.ArtistDomain
import com.majelan.androidtechnicaltest.domain.catalog.usecase.GetArtistsUseCase
import com.majelan.androidtechnicaltest.presentation.artist_list.ArtistListAction.NavigateToArtistDetails
import com.majelan.androidtechnicaltest.presentation.artist_list.ArtistListEvent.OnArtistItemClicked
import com.majelan.androidtechnicaltest.presentation.artist_list.ArtistListEvent.OnRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_list.entities.ArtistUI
import com.majelan.androidtechnicaltest.presentation.artist_list.mappers.ArtistUIMapper
import com.majelan.androidtechnicaltest.presentation.mvi.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel @Inject constructor(
   private val getArtistsUseCase: GetArtistsUseCase,
   private val mapper: ArtistUIMapper,
   @IODispatcher private val ioDispatcher: CoroutineDispatcher,
   @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
): MVIViewModel<ArtistListState, ArtistListEvent, ArtistListAction>(
   initialState = ArtistListState(
      artists = emptyList(),
      isListVisible = false,
      isErrorVisible = false,
      isLoadingVisible = false,
      isEmptyStateVisible = false,
   )
) {

   init {
      fetchArtistList()
   }

   override fun process(event: ArtistListEvent) {
      when(event) {
         is OnArtistItemClicked -> onArtistItemClicked(event.artist)
         OnRetryButtonClicked -> onRetryButtonClicked()
      }
   }

   private fun onArtistItemClicked(artist: ArtistUI) {
      emit(NavigateToArtistDetails(artist.name))
   }

   private fun onRetryButtonClicked() {
      fetchArtistList()
   }

   private fun fetchArtistList() {
      viewModelScope.launch(ioDispatcher) {
         updateState {
            it.copy(
               isLoadingVisible = true,
               isErrorVisible = false,
            )
         }
         when(val result = getArtistsUseCase()) {
            is Successful -> handleFetchArtistListSuccess(result.data)
            is Failure -> handleFetchArtistListFailure()
         }
      }
   }

   private suspend fun handleFetchArtistListSuccess(artists: List<ArtistDomain>) {
      defaultDispatcher {
         val isEmpty = artists.isEmpty()
         updateState {
            it.copy(
               artists = artists.map(mapper::map),
               isLoadingVisible = false,
               isListVisible = !isEmpty,
               isEmptyStateVisible = isEmpty,
            )
         }
      }
   }

   private fun handleFetchArtistListFailure() {
      updateState {
         it.copy(
            isErrorVisible = true,
            isLoadingVisible = false,
            isListVisible = false,
            isEmptyStateVisible = false
         )
      }
   }
}