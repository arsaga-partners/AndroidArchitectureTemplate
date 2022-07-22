package jp.arsaga.presentation.layout.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = AppTheme.colors(),
        typography = AppTheme.typography(),
        shapes = AppTheme.shapes(),
        content = content
    )
}

object AppTheme {

    @Composable
    @ReadOnlyComposable
    fun typography(): Typography = MaterialTheme.typography.copy(

    )

    @Composable
    @ReadOnlyComposable
    fun shapes(): Shapes = MaterialTheme.shapes.copy(

    )

    @Composable
    fun colors(): Colors =
        if (isSystemInDarkTheme()) AppThemeMode.Dark.colors()
        else AppThemeMode.Day.colors()
}

enum class AppThemeMode(
    val colors: @Composable () -> Colors = {
        MaterialTheme.colors.copy(

        )
    },
) {
    Day, Dark
}