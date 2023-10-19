package com.majelan.androidtechnicaltest.presentation.artist_list

import com.majelan.androidtechnicaltest.presentation.artist_list.entities.ArtistUI
import com.majelan.androidtechnicaltest.presentation.mvi.MVIAction
import com.majelan.androidtechnicaltest.presentation.mvi.MVIEvent
import com.majelan.androidtechnicaltest.presentation.mvi.MVIState

data class ArtistListState(
   val artists: List<ArtistUI>,
   val isListVisible: Boolean,
   val isErrorVisible: Boolean,
   val isLoadingVisible: Boolean,
   val isEmptyStateVisible: Boolean,
): MVIState

sealed interface ArtistListEvent: MVIEvent {
   @JvmInline
   value class OnArtistItemClicked(val artist: ArtistUI): ArtistListEvent
   object OnRetryButtonClicked: ArtistListEvent
}

sealed interface ArtistListAction: MVIAction {
   @JvmInline
   value class NavigateToArtistDetails(val name: String): ArtistListAction
}