package jp.arsaga.presentation.view.{Small}.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import jp.arsaga.domain.entity.{Small}.dto.{Large}Action
import jp.arsaga.presentation.layout.{Small}.screen.{Large}Screen
import jp.arsaga.presentation.view.{Small}.navigator.{Large}Navigator
import jp.co.arsaga.extensions.compose.screen.composable
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
    ): View = composable(R.navigation.nav_global) {
        {Large}Screen(
            {Small}Action = {Large}Action()
        )
    }
}