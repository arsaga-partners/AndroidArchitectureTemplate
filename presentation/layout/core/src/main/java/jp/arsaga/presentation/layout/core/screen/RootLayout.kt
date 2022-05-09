package jp.arsaga.presentation.layout.core.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jp.arsaga.presentation.layout.core.theme.AppTheme
import jp.arsaga.presentation.layout.core.widget.MainAppBar


@Composable
fun RootLayout(
    title: String?,
    modifier: Modifier = Modifier,
    isEnabledBack: Boolean = true,
    headerRightContent: @Composable RowScope.() -> Unit = {},
    content: @Composable PaddingValues.() -> Unit,
    backAction: () -> Unit
) {
    AppTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                title?.let {
                    MainAppBar(it, backAction, isEnabledBack, headerRightContent)
                }
            }
        ) {
            content(it)
        }
    }
}