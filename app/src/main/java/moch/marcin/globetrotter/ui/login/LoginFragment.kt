package moch.marcin.globetrotter.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import moch.marcin.globetrotter.R
import moch.marcin.globetrotter.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login, container, false
            )

        val viewModel = createViewModel("Hello")

        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun createViewModel(arg: String): LoginViewModel {
        val viewModelFactory = LoginViewModelFactory(arg)

        return ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

    }
}