package com.majelan.androidtechnicaltest.presentation.artist_details

import com.majelan.androidtechnicaltest.presentation.artist_details.entities.AlbumUI
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import com.majelan.androidtechnicaltest.presentation.mvi.MVIAction
import com.majelan.androidtechnicaltest.presentation.mvi.MVIEvent
import com.majelan.androidtechnicaltest.presentation.mvi.MVIState

data class ArtistDetailsState(
   val name: String,
   val albums: List<AlbumUI>,
   val isListVisible: Boolean,
   val isLoadingVisible: Boolean,
   val isErrorVisible: Boolean,
   val isEmptyStateVisible: Boolean,
): MVIState

sealed interface ArtistDetailsEvent: MVIEvent {
   object OnBackButtonClicked: ArtistDetailsEvent
   @JvmInline
   value class OnPlayAlbumButtonClicked(val album: AlbumUI): ArtistDetailsEvent
   @JvmInline
   value class OnMediaItemClicked(val media: MediaUI): ArtistDetailsEvent
   object OnRetryButtonClicked: ArtistDetailsEvent
}

sealed interface ArtistDetailsAction: MVIAction {
   object NavigateBack: ArtistDetailsAction
   @JvmInline
   value class NavigateToPlayer(val mediaId: String): ArtistDetailsAction
}