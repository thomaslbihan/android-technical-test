package com.majelan.androidtechnicaltest.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingView(modifier: Modifier) {
   val source = remember { MutableInteractionSource() }
   Box(
      modifier = modifier
         .background(Color.Black.copy(alpha = 0.3f))
         .clickable(interactionSource = source, indication = null) { }
   ) {
      CircularProgressIndicator(
         modifier = Modifier.align(Alignment.Center)
      )
   }
}