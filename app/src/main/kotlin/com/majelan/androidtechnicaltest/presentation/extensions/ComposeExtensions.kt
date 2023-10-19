package com.majelan.androidtechnicaltest.presentation.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.majelan.androidtechnicaltest.presentation.mvi.MVIAction
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <ACTION : MVIAction> OnAction(
   actionFlow: SharedFlow<ACTION>,
   collect: (ACTION) -> Unit
) {
   LaunchedEffect(true) {
      actionFlow.collectLatest {
         collect(it)
      }
   }
}