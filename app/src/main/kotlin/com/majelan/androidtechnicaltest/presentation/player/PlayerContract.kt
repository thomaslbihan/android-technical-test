package com.majelan.androidtechnicaltest.presentation.player

import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import com.majelan.androidtechnicaltest.presentation.mvi.MVIAction
import com.majelan.androidtechnicaltest.presentation.mvi.MVIEvent
import com.majelan.androidtechnicaltest.presentation.mvi.MVIState

data class PlayerState(
   val mediaId: String,
   val isMediaLoadingVisible: Boolean,
   val isMediaErrorVisible: Boolean,
   val picture: String,
   val title: String,
   val artistName: String,
   val artistTracks: List<MediaUI>,
   val isArtistTracksLoadingVisible: Boolean,
   val isArtistTracksListVisible: Boolean,
   val isArtistTracksEmptyStateVisible: Boolean,
   val isArtistTracksErrorVisible: Boolean,
   val genre: String,
   val recommendations: List<MediaUI>,
   val isRecommendationsLoadingVisible: Boolean,
   val isRecommendationsListVisible: Boolean,
   val isRecommendationsEmptyStateVisible: Boolean,
   val isRecommendationsErrorVisible: Boolean,
   val isPlaying: Boolean,
): MVIState

sealed interface PlayerEvent: MVIEvent {
   object OnRetryMediaButtonClicked: PlayerEvent
   object OnBackButtonClicked: PlayerEvent
   object OnPlayButtonClicked: PlayerEvent
   object OnPauseButtonClicked: PlayerEvent
   object OnArtistTracksRetryButtonClicked: PlayerEvent
   object OnRecommendationsRetryButtonClicked: PlayerEvent
   @JvmInline
   value class OnMediaItemClicked(val media: MediaUI): PlayerEvent
}

sealed interface PlayerAction: MVIAction {
   object ScrollToTop: PlayerAction
   object NavigateBack: PlayerAction
}