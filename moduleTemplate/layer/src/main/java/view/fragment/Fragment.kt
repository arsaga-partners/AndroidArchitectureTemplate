package jp.arsaga.presentation.view.{DomainPath}.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.material.Scaffold
import jp.arsaga.domain.entity.{DomainPath}.dto.{DomainUpperCamel}Action
import jp.arsaga.presentation.layout.core.widget.MainAppBar
import jp.arsaga.presentation.layout.{DomainPath}.R
import jp.arsaga.presentation.layout.{DomainPath}.screen.{DomainUpperCamel}Screen
import jp.arsaga.presentation.view.{DomainPath}.navigator.{DomainUpperCamel}Navigator
import jp.arsaga.presentation.view.core.extension.rootLayout
import jp.arsaga.presentation.viewModel.{DomainPath}.{DomainUpperCamel}ViewModel
import jp.arsaga.presentation.viewModel.core.extension.BaseViewModel


class {DomainUpperCamel}Fragment : Fragment() {

    private val viewModel by viewModels<{DomainUpperCamel}ViewModel> {
        BaseViewModel.Factory {
            {DomainUpperCamel}ViewModel(
                requireActivity().application,
                {DomainUpperCamel}Navigator
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = rootLayout(R.navigation.nav_global, "") {
        {DomainUpperCamel}Screen(
            {DomainDownerCamel}Action = {DomainUpperCamel}Action()
        )
    }
}