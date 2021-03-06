package jp.arsaga.presentation.layout.core.widget

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

enum class ButtonStyle(
    val style: TextStyle,
    val maxLines: Int = 1
) {
}

fun buttonContent(
    text: String,
    buttonStyle: ButtonStyle,
    modifier: Modifier = Modifier,
    leftIcon: @Composable RowScope.() -> Unit = {},
    rightIcon: @Composable RowScope.() -> Unit = {},
): @Composable RowScope.() -> Unit = buttonContent(
    leftIcon = leftIcon,
    rightIcon = rightIcon
) {
    Text(
        text = text,
        maxLines = buttonStyle.maxLines,
        style = buttonStyle.style,
        modifier = modifier
    )
}

fun buttonContent(
    leftIcon: @Composable RowScope.() -> Unit = {},
    rightIcon: @Composable RowScope.() -> Unit = {},
    centerIcon: @Composable RowScope.() -> Unit = {},
): @Composable RowScope.() -> Unit = {
    leftIcon(this)
    centerIcon(this)
    rightIcon(this)
}