package com.majelan.androidtechnicaltest.presentation.artist_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.AutoMirrored.Rounded
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Dimension.Companion
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.majelan.androidtechnicaltest.R
import com.majelan.androidtechnicaltest.presentation.artist_list.ArtistListAction.NavigateToArtistDetails
import com.majelan.androidtechnicaltest.presentation.artist_list.ArtistListEvent.OnArtistItemClicked
import com.majelan.androidtechnicaltest.presentation.artist_list.ArtistListEvent.OnRetryButtonClicked
import com.majelan.androidtechnicaltest.presentation.artist_list.entities.ArtistUI
import com.majelan.androidtechnicaltest.presentation.common.EmptyState
import com.majelan.androidtechnicaltest.presentation.common.ErrorView
import com.majelan.androidtechnicaltest.presentation.common.LoadingView
import com.majelan.androidtechnicaltest.presentation.extensions.OnAction
import com.majelan.androidtechnicaltest.presentation.extensions.centerHorizontallyTo
import com.majelan.androidtechnicaltest.presentation.extensions.centerVerticallyTo
import com.majelan.androidtechnicaltest.presentation.navigation.Destination
import com.majelan.androidtechnicaltest.presentation.navigation.Destination.ArtistDetails

@Composable
fun ArtistListScreen(
   viewModel: ArtistListViewModel = hiltViewModel(),
   navController: NavController,
) {
   val state by viewModel.stateFlow.collectAsStateWithLifecycle()

   OnAction(viewModel.actionFlow) {
      when(it) {
         is NavigateToArtistDetails -> navController.navigate(ArtistDetails(it.name).route)
      }
   }

   ConstraintLayout(modifier = Modifier.fillMaxSize()) {
      val (titleRef, listRef, emptyStateRef, loadingRef, errorRef) = createRefs()
      Text(
         modifier = Modifier.constrainAs(titleRef) {
            width = Companion.fillToConstraints
            top.linkTo(parent.top, 32.dp)
            centerHorizontallyTo(parent, margin = 16.dp)
         },
         text = stringResource(id = R.string.artist_list_title),
         style = MaterialTheme.typography.titleLarge,
         textAlign = TextAlign.Start,
      )

      if (state.isListVisible) {
         List(
            modifier = Modifier.constrainAs(listRef) {
               width = Companion.fillToConstraints
               height = Companion.fillToConstraints
               top.linkTo(titleRef.bottom)
               bottom.linkTo(parent.bottom)
               centerHorizontallyTo(parent)
            },
            artists = state.artists,
            onItemClicked = { viewModel.process(OnArtistItemClicked(it)) }
         )
      }

      if (state.isLoadingVisible) {
         LoadingView(
            modifier = Modifier.constrainAs(loadingRef) {
               width = Companion.fillToConstraints
               height = Companion.fillToConstraints
               centerHorizontallyTo(parent)
               centerVerticallyTo(parent)
            }
         )
      }

      if (state.isEmptyStateVisible) {
         EmptyState(
            modifier = Modifier.constrainAs(emptyStateRef) {
               width = Companion.fillToConstraints
               height = Companion.fillToConstraints
               top.linkTo(titleRef.bottom)
               bottom.linkTo(parent.bottom)
               centerHorizontallyTo(parent)
            },
            text = stringResource(id = R.string.artist_list_empty),
         )
      }

      if (state.isErrorVisible) {
         ErrorView(
            modifier = Modifier.constrainAs(errorRef) {
               width = Companion.fillToConstraints
               height = Companion.fillToConstraints
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
private fun List(
   modifier: Modifier,
   artists: List<ArtistUI>,
   onItemClicked: (ArtistUI) -> Unit
) {
   LazyColumn(
      modifier = modifier,
   ) {
      items(artists, key = { it.name }) { artist ->
         Item(
            modifier = Modifier.fillMaxWidth(),
            artist = artist,
            onClick = { onItemClicked(artist) }
         )
      }
   }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Item(modifier: Modifier, artist: ArtistUI, onClick: () -> Unit) {
   ConstraintLayout(modifier = modifier.clickable { onClick() }) {
      val (pictureRef, albumCountRef, nameRef, chevRightRef) = createRefs()

      GlideImage(
         modifier = Modifier
            .constrainAs(pictureRef) {
               width = Dimension.value(100.dp)
               height = Companion.value(100.dp)
               start.linkTo(parent.start, margin = 16.dp)
               centerVerticallyTo(parent, margin = 16.dp)
            }
            .clip(RoundedCornerShape(16.dp)),
         model = artist.picture,
         contentDescription = "",
         contentScale = ContentScale.Crop,
      )

      createVerticalChain(nameRef, albumCountRef, chainStyle = ChainStyle.Packed)
      Text(
         modifier = Modifier.constrainAs(nameRef) {
            width = Companion.fillToConstraints
            start.linkTo(pictureRef.end, margin = 8.dp)
            end.linkTo(chevRightRef.start, margin = 8.dp)
         },
         text = artist.name,
         style = MaterialTheme.typography.titleSmall,
         textAlign = TextAlign.Start,
      )

      Text(
         modifier = Modifier.constrainAs(albumCountRef) {
            width = Companion.fillToConstraints
            start.linkTo(pictureRef.end, margin = 8.dp)
            end.linkTo(chevRightRef.start, margin = 8.dp)
         },
         text = pluralStringResource(id = R.plurals.artist_list_album_count, count = artist.albumCount, artist.albumCount),
         textAlign = TextAlign.Start,
         style = MaterialTheme.typography.labelMedium,
      )

      Icon(
         modifier = Modifier.constrainAs(chevRightRef) {
            end.linkTo(parent.end, margin = 16.dp)
            centerVerticallyTo(parent)
         },
         imageVector = Rounded.KeyboardArrowRight,
         contentDescription = ""
      )
   }
}
