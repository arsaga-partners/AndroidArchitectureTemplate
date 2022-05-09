package jp.arsaga.presentation.layout.core.widget

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier



@Composable
fun MainAppBar(
    title: String,
    isEnabledBack: Boolean = false,
    isEnabledClose: Boolean = false,
) {
    MainAppBar(isEnabledBack, isEnabledClose) {
        Text(text = title)
    }
}

@Composable
fun MainAppBar(
    isEnabledBack: Boolean = false,
    isEnabledClose: Boolean = false,
    centerContent: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        centerContent(this)
    }
}