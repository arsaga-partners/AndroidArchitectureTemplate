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
        colors = colors(),
        typography = typography(),
        shapes = shapes(),
        content = content
    )
}

@Composable
private fun colors(): Colors = MaterialTheme.colors.copy(

)

@Composable
private fun typography(): Typography = MaterialTheme.typography.copy(

)

@Composable
private fun shapes(): Shapes = MaterialTheme.shapes.copy(

)