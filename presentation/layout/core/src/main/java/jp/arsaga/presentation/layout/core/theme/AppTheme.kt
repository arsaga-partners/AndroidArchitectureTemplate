package jp.arsaga.presentation.layout.core.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = AppTheme.colors(LocalContext.current),
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
    fun colors(context: Context): Colors =
        if (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
            AppThemeMode.Dark.colors()
        else AppThemeMode.Day.colors()
}

enum class AppThemeMode(
    val colors: @Composable () -> Colors = {
        MaterialTheme.colors.copy(

        )
    }
) {
    Day, Dark
}