package jp.arsaga.presentation.layout.core.widget

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.fragment.app.Fragment
import jp.arsaga.presentation.layout.core.theme.AppTheme


@Composable
fun MainAppBar(
    title: String?,
    backAction: () -> Unit,
    isEnabledBack: Boolean = false,
    style: TextStyle = AppTheme.typography().h1,
    rightContent: @Composable RowScope.() -> Unit = {}
) {
    MainAppBar(
        leftContent = {
            if (isEnabledBack) IconButton(onClick = backAction) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        centerContent = {
            title?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    style = style,
                    modifier = Modifier.weight(1F)
                )
            }
        }, rightContent = rightContent
    )
}

@Composable
fun MainAppBar(
    leftContent: @Composable RowScope.() -> Unit,
    centerContent: @Composable RowScope.() -> Unit,
    rightContent: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        leftContent(this)
        centerContent(this)
        rightContent(this)
    }
}