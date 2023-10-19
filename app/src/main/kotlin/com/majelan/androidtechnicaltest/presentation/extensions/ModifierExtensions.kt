package com.majelan.androidtechnicaltest.presentation.extensions

import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference

fun ConstrainScope.centerVerticallyTo(other: ConstrainedLayoutReference, margin: Dp) {
   top.linkTo(other.top, margin = margin)
   bottom.linkTo(other.bottom, margin = margin)
}

fun ConstrainScope.centerHorizontallyTo(other: ConstrainedLayoutReference, margin: Dp) {
   start.linkTo(other.start, margin = margin)
   end.linkTo(other.end, margin = margin)
}