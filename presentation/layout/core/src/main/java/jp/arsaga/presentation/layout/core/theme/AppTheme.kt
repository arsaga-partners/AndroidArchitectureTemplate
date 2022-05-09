package jp.arsaga.presentation.layout.core.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
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
    fun colors(): Colors = MaterialTheme.colors.copy(

    )

    @Composable
    fun typography(): Typography = MaterialTheme.typography.copy(

    )

    @Composable
    fun shapes(): Shapes = MaterialTheme.shapes.copy(

    )
}