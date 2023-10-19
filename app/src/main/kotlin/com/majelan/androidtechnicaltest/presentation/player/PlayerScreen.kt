package com.majelan.androidtechnicaltest.presentation.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Dimension.Companion
import androidx.core.widget.ContentLoadingProgressBar
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.majelan.androidtechnicaltest.R
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import com.majelan.androidtechnicaltest.presentation.common.EmptyState
import com.majelan.androidtechnicaltest.presentation.common.ErrorView
import com.majelan.androidtechnicaltest.presentation.extensions.OnAction
import com.majelan.androidtechnicaltest.presentation.extensions.centerHorizontallyTo
import com.majelan.androidtechnicaltest.presentation.player.PlayerAction.NavigateBack
import com.majelan.androidtechnicaltest.presentation.player.PlayerAction.ScrollToTop
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnArtistTracksRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnMediaItemClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnPauseButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnPlayButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnRecommendationsRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnRetryMediaButtonClicked
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(
   viewModel: PlayerViewModel = hiltViewModel(),
   navController: NavController,
) {
   val state by viewModel.stateFlow.collectAsStateWithLifecycle()
   val scrollState = rememberScrollState()
   val coroutineScope = rememberCoroutineScope()

   OnAction(viewModel.actionFlow) {
      when(it) {
         ScrollToTop -> coroutineScope.launch {
            scrollState.animateScrollTo(0)
         }
         NavigateBack -> navController.popBackStack()
      }
   }

   ConstraintLayout(
      modifier = Modifier
         .fillMaxSize()
         .verticalScroll(scrollState)
   ) {
      val (playerRef, artistTracksRef, recommendationsRef) = createRefs()

      val configuration = LocalConfiguration.current
      val playerHeight = (configuration.screenHeightDp * 0.8f).dp
      Player(
         modifier = Modifier.constrainAs(playerRef) {
            width = Dimension.fillToConstraints
            height = Companion.value(playerHeight)
            centerHorizontallyTo(parent)
            top.linkTo(parent.top)
         },
         picture = state.picture,
         title = state.title,
         artistName = state.artistName,
         isPlaying = state.isPlaying,
         isErrorVisible = state.isMediaErrorVisible,
         isLoadingVisible = state.isMediaLoadingVisible,
         onPlayButtonClicked = { viewModel.process(OnPlayButtonClicked) },
         onPauseButtonClicked = { viewModel.process(OnPauseButtonClicked) },
         onRetryButtonClicked = { viewModel.process(OnRetryMediaButtonClicked) },
      )

      Section(
         modifier = Modifier.constrainAs(artistTracksRef) {
            width = Companion.fillToConstraints
            top.linkTo(playerRef.bottom, margin = 16.dp)
            centerHorizontallyTo(parent)
         },
         title = stringResource(id = R.string.player_artist_tracks_title, state.artistName),
         emptyStateText = stringResource(id = R.string.player_artist_tracks_empty, state.artistName),
         medias = state.artistTracks,
         isListVisible = state.isArtistTracksListVisible,
         isLoadingVisible = state.isArtistTracksLoadingVisible,
         isErrorVisible = state.isArtistTracksErrorVisible,
         isEmptyStateVisible = state.isArtistTracksEmptyStateVisible,
         onMediaClicked = { viewModel.process(OnMediaItemClicked(it)) },
         onRetryButtonClicked = { viewModel.process(OnArtistTracksRetryButtonClicked) }
      )

      Section(
         modifier = Modifier.constrainAs(recommendationsRef) {
            width = Companion.fillToConstraints
            top.linkTo(artistTracksRef.bottom, margin = 16.dp)
            centerHorizontallyTo(parent)
            bottom.linkTo(parent.bottom, margin = 32.dp)
         },
         title = stringResource(id = R.string.player_recommendations_title),
         emptyStateText = stringResource(id = R.string.player_recommendations_empty),
         medias = state.recommendations,
         isListVisible = state.isRecommendationsListVisible,
         isLoadingVisible = state.isRecommendationsLoadingVisible,
         isErrorVisible = state.isRecommendationsErrorVisible,
         isEmptyStateVisible = state.isRecommendationsEmptyStateVisible,
         onMediaClicked = { viewModel.process(OnMediaItemClicked(it)) },
         onRetryButtonClicked = { viewModel.process(OnRecommendationsRetryButtonClicked) }
      )
   }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Player(
   modifier: Modifier,
   picture: String,
   title: String,
   artistName: String,
   isPlaying: Boolean,
   isErrorVisible: Boolean,
   isLoadingVisible: Boolean,
   onPlayButtonClicked: () -> Unit,
   onPauseButtonClicked: () -> Unit,
   onRetryButtonClicked: () -> Unit,
) {
   ConstraintLayout(modifier = modifier) {
      val (
         pictureRef,
         titleRef,
         artistNameRef,
         playPauseButtonRef,
         loadingRef,
         errorRef,
      ) = createRefs()

      GlideImage(
         modifier = Modifier.constrainAs(pictureRef) {
            width = Dimension.fillToConstraints
            height = Dimension.percent(0.8f)
            centerHorizontallyTo(parent)
            top.linkTo(parent.top)
         },
         model = picture,
         contentDescription = "",
         contentScale = ContentScale.Crop,
      )

      Text(
         modifier = Modifier.constrainAs(titleRef) {
            width = Companion.fillToConstraints
            top.linkTo(pictureRef.bottom, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
            end.linkTo(playPauseButtonRef.start, margin = 8.dp)
         },
         text = title,
         maxLines = 1,
         overflow = TextOverflow.Ellipsis,
         style = MaterialTheme.typography.titleMedium,
         textAlign = TextAlign.Start,
      )

      Text(
         modifier = Modifier.constrainAs(artistNameRef) {
            width = Companion.fillToConstraints
            start.linkTo(parent.start, margin = 16.dp)
            end.linkTo(playPauseButtonRef.start, margin = 8.dp)
            top.linkTo(titleRef.bottom, margin = 8.dp)
         },
         text = artistName,
         maxLines = 1,
         overflow = TextOverflow.Ellipsis,
         style = MaterialTheme.typography.labelMedium,
         textAlign = TextAlign.Start,
      )

      FloatingActionButton(
         modifier = Modifier.constrainAs(playPauseButtonRef) {
            end.linkTo(parent.end, margin = 16.dp)
            bottom.linkTo(parent.bottom, margin = 16.dp)
            top.linkTo(pictureRef.bottom)
            bottom.linkTo(pictureRef.bottom)
         },
         onClick = {
            if (isPlaying) {
               onPauseButtonClicked()
            } else onPlayButtonClicked()
         }
      ) {
         Icon(
            imageVector = if (isPlaying) {
               Rounded.Pause
            } else Rounded.PlayArrow,
            contentDescription = ""
         )
      }

      if (isLoadingVisible) {
         CircularProgressIndicator(
            modifier = Modifier.constrainAs(loadingRef) {
               centerVerticallyTo(parent)
               centerHorizontallyTo(parent)
            }
         )
      }

      if (isErrorVisible) {
         ErrorView(
            modifier = Modifier.constrainAs(errorRef) {
               centerVerticallyTo(parent)
               centerHorizontallyTo(parent)
            },
            onClick = onRetryButtonClicked
         )
      }
   }
}

@Composable
private fun Section(
   modifier: Modifier,
   title: String,
   emptyStateText: String,
   medias: List<MediaUI>,
   isListVisible: Boolean,
   isLoadingVisible: Boolean,
   isErrorVisible: Boolean,
   isEmptyStateVisible: Boolean,
   onMediaClicked: (MediaUI) -> Unit,
   onRetryButtonClicked: () -> Unit,
) {
   ConstraintLayout(modifier = modifier) {
      val (
         titleRef,
         listRef,
         emptyStateRef,
         loadingRef,
         errorRef
      ) = createRefs()

      Text(
         modifier = Modifier.constrainAs(titleRef) {
            start.linkTo(parent.start, margin = 16.dp)
            top.linkTo(parent.top)
         },
         text = title,
         style = MaterialTheme.typography.titleLarge,
      )

      if (isListVisible) {
         Column(
            modifier = Modifier.constrainAs(listRef) {
               width = Companion.fillToConstraints
               top.linkTo(titleRef.bottom, margin = 16.dp)
               centerHorizontallyTo(parent)
            }
         ) {
            medias.forEach { media ->
               MediaItem(
                  modifier = Modifier.fillMaxWidth(),
                  media = media,
                  onClick = { onMediaClicked(media) },
               )
            }
         }
      }

      if (isEmptyStateVisible) {
         EmptyState(
            modifier = Modifier.constrainAs(emptyStateRef) {
               width = Companion.fillToConstraints
               centerHorizontallyTo(parent)
               top.linkTo(titleRef.bottom, margin = 16.dp)
            },
            text = emptyStateText,
         )
      }

      if (isErrorVisible) {
         ErrorView(
            modifier = Modifier.constrainAs(errorRef) {
               width = Companion.fillToConstraints
               top.linkTo(titleRef.bottom, margin = 16.dp)
               centerHorizontallyTo(parent)
            },
            onClick = onRetryButtonClicked
         )
      }

      if (isLoadingVisible) {
         CircularProgressIndicator(
            modifier = Modifier.constrainAs(loadingRef) {
               centerHorizontallyTo(parent)
               top.linkTo(titleRef.bottom, margin = 16.dp)
            }
         )
      }
   }
}

@Composable
fun MediaItem(
   modifier: Modifier,
   media: MediaUI,
   onClick: () -> Unit,
) {
   Row(
      modifier = modifier
         .clickable { onClick() }
         .padding(vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
   ) {
      Text(
         modifier = Modifier
            .padding(horizontal = 16.dp)
            .weight(1f),
         text = media.name,
         maxLines = 1,
         overflow = TextOverflow.Ellipsis,
         style = MaterialTheme.typography.bodyMedium
      )
      Icon(
         modifier = Modifier.padding(end = 16.dp),
         imageVector = Rounded.PlayArrow,
         contentDescription = ""
      )
   }
}