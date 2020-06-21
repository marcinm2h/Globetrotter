package moch.marcin.globetrotter.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import moch.marcin.globetrotter.MainActivity
import moch.marcin.globetrotter.OnLogin
import moch.marcin.globetrotter.R
import moch.marcin.globetrotter.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), OnLogin {

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


        viewModel.navigationActionEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val action = when (it) {
//                    NavigationActions.LOGIN -> LoginFragmentDirections.login()
//                    NavigationActions.LOGIN -> createSignInIntent()
                    NavigationActions.LOGIN -> {
                        when (val activity = requireActivity()) {
                            is OnLogin -> activity.onLogin()
                            else -> onLogin()
                        }
                    }
                }
//                findNavController().navigate(action)
                viewModel.doneNavigation()
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun createViewModel(arg: String): LoginViewModel {
        val viewModelFactory = LoginViewModelFactory(arg)

        return ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

    }

    // https://github.com/firebase/snippets-android/blob/e16846813135fde4fd6e8823948cfae61e17fd57/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/MainActivity.kt
    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.uid
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onLogin() {
        TODO("not implemented")
    }
}
