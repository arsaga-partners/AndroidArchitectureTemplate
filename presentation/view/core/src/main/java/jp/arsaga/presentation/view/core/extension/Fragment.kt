package jp.arsaga.presentation.view.core.extension

import android.view.View
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.arsaga.presentation.layout.core.screen.RootLayout
import jp.co.arsaga.extensions.compose.screen.composable

fun Fragment.rootLayout(
    @StringRes titleId: Int,
    modifier: Modifier = Modifier,
    isEnabledBack: Boolean = true,
    headerRightContent: @Composable RowScope.() -> Unit = {},
    content: @Composable PaddingValues.() -> Unit
): View = composable(titleId) {
    RootLayout(getString(titleId), modifier, isEnabledBack, headerRightContent, content) {
        findNavController().navigateUp()
    }
}

fun Fragment.rootLayout(
    navFragmentId: Int,
    title: String?,
    modifier: Modifier = Modifier,
    isEnabledBack: Boolean = true,
    headerRightContent: @Composable RowScope.() -> Unit = {},
    content: @Composable PaddingValues.() -> Unit
): View = composable(navFragmentId) {
    RootLayout(title, modifier, isEnabledBack, headerRightContent, content) {
        findNavController().navigateUp()
    }
}