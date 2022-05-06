package jp.arsaga.presentation.layout.core.extension

import androidx.compose.ui.unit.Dp


val Int.unitDp: Dp get() = Dp(value = this.times(8).toFloat())
val Double.unitDp: Dp get() = Dp(value = this.times(8).toFloat())
