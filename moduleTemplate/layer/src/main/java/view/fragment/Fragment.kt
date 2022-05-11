package jp.arsaga.presentation.view.{Small}.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.material.Scaffold
import jp.arsaga.domain.entity.{Small}.dto.{Large}Action
import jp.arsaga.presentation.layout.core.widget.MainAppBar
import jp.arsaga.presentation.layout.{Small}.R
import jp.arsaga.presentation.layout.{Small}.screen.{Large}Screen
import jp.arsaga.presentation.view.{Small}.navigator.{Large}Navigator
import jp.arsaga.presentation.view.core.extension.rootLayout
import jp.arsaga.presentation.viewModel.{Small}.{Large}ViewModel
import jp.arsaga.presentation.viewModel.core.extension.BaseViewModel


class {Large}Fragment : Fragment() {

    private val viewModel by viewModels<{Large}ViewModel> {
        BaseViewModel.Factory {
            {Large}ViewModel(
                requireActivity().application,
                {Large}Navigator
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = rootLayout(R.navigation.nav_global, "") {
        {Large}Screen(
            {Medium}Action = {Large}Action()
        )
    }
}