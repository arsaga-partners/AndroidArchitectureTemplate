package jp.arsaga.presentation.layout.{DomainPath}.screen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import jp.arsaga.domain.entity.{DomainPath}.dto.{DomainUpperCamel}Action
import jp.arsaga.presentation.layout.{DomainPath}.widget.{DomainUpperCamel}Widget


@Composable
fun {DomainUpperCamel}Screen(
    {DomainDownerCamel}Action: {DomainUpperCamel}Action
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        {DomainUpperCamel}Widget()
    }
}