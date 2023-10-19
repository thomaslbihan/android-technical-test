package com.majelan.androidtechnicaltest.presentation.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Dimension.Companion
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.majelan.androidtechnicaltest.R
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import com.majelan.androidtechnicaltest.presentation.common.EmptyState
import com.majelan.androidtechnicaltest.presentation.common.ErrorView
import com.majelan.androidtechnicaltest.presentation.extensions.OnAction
import com.majelan.androidtechnicaltest.presentation.extensions.centerHorizontallyTo
import com.majelan.androidtechnicaltest.presentation.player.PlayerAction.NavigateBack
import com.majelan.androidtechnicaltest.presentation.player.PlayerAction.ScrollToTop
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnArtistTracksRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnBackButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnHardwareBackPressed
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnMediaItemClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnPauseButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnPlayButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnRecommendationsRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.player.PlayerEvent.OnRetryMediaButtonClicked
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
   viewModel: PlayerViewModel = hiltViewModel(),
   navController: NavController,
) {
   val state by viewModel.stateFlow.collectAsStateWithLifecycle()
   val scrollState = rememberScrollState()
   val coroutineScope = rememberCoroutineScope()

   OnAction(viewModel.actionFlow) {
      when (it) {
         ScrollToTop -> coroutineScope.launch {
            scrollState.animateScrollTo(0)
         }

         NavigateBack -> navController.popBackStack()
      }
   }

   BackHandler {
      viewModel.process(OnHardwareBackPressed)
   }

   Column(modifier = Modifier.fillMaxSize()) {
      TopAppBar(
         modifier = Modifier.fillMaxWidth(),
         title = {},
         navigationIcon = {
            IconButton(onClick = { viewModel.process(OnBackButtonClicked) }) {
               Icon(
                  imageVector = AutoMirrored.Rounded.ArrowBack,
                  contentDescription = ""
               )
            }
         }
      )

      ConstraintLayout(
         modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .verticalScroll(scrollState)
      ) {
         val (playerRef, artistTracksRef, recommendationsRef) = createRefs()

         Player(
            modifier = Modifier.constrainAs(playerRef) {
               width = Dimension.fillToConstraints
               centerHorizontallyTo(parent, margin = 16.dp)
               top.linkTo(parent.top, margin = 8.dp)
            },
            picture = state.picture,
            title = state.title,
            songUri = state.songUri,
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
               top.linkTo(playerRef.bottom, margin = 48.dp)
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
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Player(
   modifier: Modifier,
   picture: String,
   title: String,
   songUri: String,
   artistName: String,
   isPlaying: Boolean,
   isErrorVisible: Boolean,
   isLoadingVisible: Boolean,
   onPlayButtonClicked: () -> Unit,
   onPauseButtonClicked: () -> Unit,
   onRetryButtonClicked: () -> Unit,
) {
   val infiniteTransition = rememberInfiniteTransition(label = "rotation")
   val rotation by infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 360f,
      animationSpec = infiniteRepeatable(animation = tween(10_000, easing = LinearEasing)),
      label = ""
   )

   Card(modifier = modifier) {
      ConstraintLayout(modifier = Modifier.fillMaxSize()) {
         val (
            pictureRef,
            titleRef,
            artistNameRef,
            playPauseButtonRef,
            loadingRef,
            errorRef,
         ) = createRefs()

         AudioPlayer(uri = songUri, isPlaying = isPlaying)

         GlideImage(
            modifier = Modifier
               .constrainAs(pictureRef) {
                  width = Dimension.percent(0.8f)
                  height = Companion.ratio("1:1")
                  centerHorizontallyTo(parent)
                  top.linkTo(parent.top, margin = 16.dp)
               }
               .rotate(if (isPlaying) rotation else 0f)
               .clip(CircleShape),
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
               bottom.linkTo(parent.bottom, margin = 16.dp)
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
               top.linkTo(pictureRef.bottom, margin = 8.dp)
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
}

@Composable
fun AudioPlayer(
   uri: String,
   isPlaying: Boolean,
) {
   val context = LocalContext.current
   val player = remember {
      ExoPlayer.Builder(context)
         .build()
         .apply {
            playWhenReady = true
         }
   }

   LaunchedEffect(uri) {
      player.setMediaItem(MediaItem.fromUri(uri))
      player.prepare()
   }

   LaunchedEffect(isPlaying) {
      if (isPlaying) {
         player.play()
      } else {
         player.pause()
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
            width = Companion.fillToConstraints
            centerHorizontallyTo(parent, margin = 16.dp)
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