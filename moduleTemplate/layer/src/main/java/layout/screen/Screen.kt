package jp.arsaga.presentation.layout.{Small}.screen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import jp.arsaga.domain.entity.{Small}.dto.{Large}Action
import jp.arsaga.presentation.layout.{Small}.widget.{Large}Widget


@Composable
fun {Large}Screen(
    {Small}Action: {Large}Action
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        {Large}Widget()
    }
}