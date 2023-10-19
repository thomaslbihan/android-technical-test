package com.majelan.androidtechnicaltest.presentation.artist_details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Dimension.Companion
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.majelan.androidtechnicaltest.R
import com.majelan.androidtechnicaltest.R.string
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsAction.NavigateBack
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsAction.NavigateToPlayer
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnBackButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnMediaItemClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnPlayAlbumButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.ArtistDetailsEvent.OnRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.AlbumUI
import com.majelan.androidtechnicaltest.presentation.artist_details.entities.MediaUI
import com.majelan.androidtechnicaltest.presentation.common.EmptyState
import com.majelan.androidtechnicaltest.presentation.common.ErrorView
import com.majelan.androidtechnicaltest.presentation.common.LoadingView
import com.majelan.androidtechnicaltest.presentation.extensions.OnAction
import com.majelan.androidtechnicaltest.presentation.extensions.centerHorizontallyTo
import com.majelan.androidtechnicaltest.presentation.navigation.Destination.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailsScreen(
   viewModel: ArtistDetailsViewModel = hiltViewModel(),
   navController: NavController
) {
   val state by viewModel.stateFlow.collectAsStateWithLifecycle()

   OnAction(viewModel.actionFlow) {
      when(it) {
         NavigateBack -> navController.popBackStack()
         is NavigateToPlayer -> navController.navigate(Player(it.mediaId).route)
      }
   }

   ConstraintLayout(modifier = Modifier.fillMaxSize()) {
      val (topAppBarRef, titleRef, listRef, emptyStateRef, loadingRef, errorRef) = createRefs()

      TopAppBar(
         modifier = Modifier.constrainAs(topAppBarRef) {
            width = Dimension.fillToConstraints
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
         },
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

      Text(
         modifier = Modifier.constrainAs(titleRef) {
            width = Dimension.fillToConstraints
            top.linkTo(topAppBarRef.bottom, 8.dp)
            centerHorizontallyTo(parent, margin = 16.dp)
         },
         text = stringResource(id = string.artist_details_title, state.name),
         style = MaterialTheme.typography.titleLarge,
         textAlign = TextAlign.Start,
      )

      if (state.isListVisible) {
         AlbumList(
            modifier = Modifier.constrainAs(listRef) {
               width = Dimension.fillToConstraints
               height = Dimension.fillToConstraints
               top.linkTo(titleRef.bottom, margin = 8.dp)
               bottom.linkTo(parent.bottom)
               centerHorizontallyTo(parent)
            },
            albums = state.albums,
            onPlayAlbumButtonClicked = { viewModel.process(OnPlayAlbumButtonClicked(it)) },
            onItemClicked = { viewModel.process(OnMediaItemClicked(it)) }
         )
      }

      if (state.isLoadingVisible) {
         LoadingView(
            modifier = Modifier.constrainAs(loadingRef) {
               width = Dimension.fillToConstraints
               height = Dimension.fillToConstraints
               centerHorizontallyTo(parent)
               centerVerticallyTo(parent)
            }
         )
      }

      if (state.isEmptyStateVisible) {
         EmptyState(
            modifier = Modifier.constrainAs(emptyStateRef) {
               width = Dimension.fillToConstraints
               height = Dimension.fillToConstraints
               top.linkTo(titleRef.bottom)
               bottom.linkTo(parent.bottom)
               centerHorizontallyTo(parent)
            },
            text = stringResource(id = string.artist_details_empty)
         )
      }

      if (state.isErrorVisible) {
         ErrorView(
            modifier = Modifier.constrainAs(errorRef) {
               width = Dimension.fillToConstraints
               height = Dimension.fillToConstraints
               top.linkTo(titleRef.bottom)
               bottom.linkTo(parent.bottom)
               centerHorizontallyTo(parent)
            },
            onClick = { viewModel.process(OnRetryButtonClicked) }
         )
      }
   }
}


@Composable
private fun AlbumList(
   modifier: Modifier,
   albums: List<AlbumUI>,
   onPlayAlbumButtonClicked: (AlbumUI) -> Unit,
   onItemClicked: (MediaUI) -> Unit
) {
   LazyColumn(
      modifier = modifier,
   ) {
      items(albums, key = { it.name }) { album ->
         Album(
            modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = 16.dp, vertical = 8.dp),
            album = album,
            onPlayAlbumButtonClicked = { onPlayAlbumButtonClicked(album) },
            onClick = onItemClicked,
         )
      }
   }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Album(
   modifier: Modifier,
   album: AlbumUI,
   onPlayAlbumButtonClicked: () -> Unit,
   onClick: (MediaUI) -> Unit
) {
   Card(modifier = modifier) {
      ConstraintLayout {
         val (
            pictureRef,
            trackCountRef,
            durationRef,
            nameRef,
            mediaListRef,
            playBtnRef,
         ) = createRefs()

         GlideImage(
            modifier = Modifier
               .constrainAs(pictureRef) {
                  width = Dimension.value(50.dp)
                  height = Dimension.value(50.dp)
                  start.linkTo(parent.start, margin = 16.dp)
                  top.linkTo(parent.top, margin = 16.dp)
               }
               .clip(RoundedCornerShape(8.dp)),
            model = album.picture,
            contentDescription = "",
            contentScale = ContentScale.Crop,
         )

         Text(
            modifier = Modifier.constrainAs(nameRef) {
               width = Companion.fillToConstraints
               start.linkTo(pictureRef.end, margin = 8.dp)
               end.linkTo(playBtnRef.start, margin = 8.dp)
               top.linkTo(pictureRef.top)
               bottom.linkTo(trackCountRef.top)
            },
            text = album.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
         )

         Text(
            modifier = Modifier.constrainAs(trackCountRef) {
               start.linkTo(pictureRef.end, margin = 8.dp)
               top.linkTo(nameRef.bottom)
               bottom.linkTo(pictureRef.bottom)
            },
            text = pluralStringResource(id = R.plurals.artist_details_track_count, count = album.totalTrackCount, album.totalTrackCount),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Start,
         )

         Text(
            modifier = Modifier.constrainAs(durationRef) {
               baseline.linkTo(trackCountRef.baseline)
               start.linkTo(trackCountRef.end, margin = 16.dp)
            },
            text = album.formattedTotalDuration,
            style = MaterialTheme.typography.labelSmall,
         )

         FloatingActionButton(
            modifier = Modifier.constrainAs(playBtnRef) {
               end.linkTo(parent.end, margin = 16.dp)
               centerVerticallyTo(pictureRef)
            },
            onClick = onPlayAlbumButtonClicked,
            elevation = FloatingActionButtonDefaults.loweredElevation()
         ) {
            Icon(
               imageVector = Rounded.PlayArrow,
               contentDescription = ""
            )
         }

         MediaList(
            modifier = Modifier.constrainAs(mediaListRef) {
               centerHorizontallyTo(parent)
               top.linkTo(pictureRef.bottom, margin = 16.dp)
               bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            medias = album.medias,
            onItemClicked = onClick
         )
      }
   }
}

@Composable
private fun MediaList(
   modifier: Modifier,
   medias: List<MediaUI>,
   onItemClicked: (MediaUI) -> Unit,
) {
   Column(modifier = modifier) {
      medias.forEach { media ->
         Media(
            modifier = Modifier.fillMaxWidth(),
            media = media,
            onClick = { onItemClicked(media) }
         )
      }
   }
}

@Composable
private fun Media(
   modifier: Modifier,
   media: MediaUI,
   onClick: () -> Unit
) {
   Row(
      modifier = modifier
         .clickable { onClick() }
         .padding(vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
   ) {
      Text(
         modifier = Modifier
            .width(40.dp)
            .padding(start = 8.dp),
         text = media.formattedTrackNumber,
         textAlign = TextAlign.Center,
         style = MaterialTheme.typography.bodyMedium
      )
      Text(
         modifier = Modifier
            .padding(end = 16.dp)
            .weight(1f),
         text = media.name,
         maxLines = 1,
         overflow = TextOverflow.Ellipsis,
         style = MaterialTheme.typography.bodyMedium
      )
      Icon(
         modifier = Modifier.padding(end = 16.dp),
         imageVector = Rounded.ChevronRight,
         contentDescription = ""
      )
   }
}
