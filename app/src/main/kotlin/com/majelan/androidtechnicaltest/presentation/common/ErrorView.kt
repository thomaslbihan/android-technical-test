package com.majelan.androidtechnicaltest.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.majelan.androidtechnicaltest.R.string

@Composable
fun ErrorView(
   modifier: Modifier,
   onClick: () -> Unit,
) {
   Column(
      modifier = modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
   ) {
      Text(
         text = stringResource(id = string.artist_list_error),
         style = MaterialTheme.typography.bodyMedium,
      )

      Button(onClick = onClick) {
         Text(text = stringResource(id = string.common_retry))
      }
   }
}
