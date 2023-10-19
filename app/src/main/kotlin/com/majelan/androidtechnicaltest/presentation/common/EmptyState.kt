package com.majelan.androidtechnicaltest.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EmptyState(
   modifier: Modifier,
   text: String,
) {
   Box(modifier = modifier) {
      Text(
         modifier = Modifier.align(Alignment.Center),
         text = text,
         style = MaterialTheme.typography.bodyMedium,
      )
   }
}