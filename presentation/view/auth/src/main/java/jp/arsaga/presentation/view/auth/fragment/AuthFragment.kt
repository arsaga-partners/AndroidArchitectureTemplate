package jp.arsaga.presentation.view.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import jp.arsaga.domain.useCase.auth.AuthUseCase
import jp.arsaga.domain.useCase.core.ActivityCallback
import jp.arsaga.presentation.view.auth.R
import jp.arsaga.presentation.view.core.extension.rootLayout
import jp.arsaga.presentation.viewModel.auth.AuthViewModel
import jp.arsaga.presentation.viewModel.core.extension.BaseViewModel
import jp.co.arsaga.extensions.view.getNavController

class AuthFragment : Fragment() {

    class AuthNavigator : AuthUseCase.Navigator<ActivityCallback> {
        override val successLogin: ActivityCallback = {
            it.getNavController()
                ?.navigate(AuthFragmentDirections.actionLoginFragmentToMainActivity())
        }
    }

    private val viewModel by viewModels<AuthViewModel> {
        BaseViewModel.Factory {
            AuthViewModel(
                requireActivity().application,
                AuthNavigator()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = rootLayout(R.id.loginFragment, "") {
        Text (text = "Hello Masaki!")
    }
}