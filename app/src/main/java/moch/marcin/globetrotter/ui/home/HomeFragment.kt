package moch.marcin.globetrotter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import moch.marcin.globetrotter.R
import moch.marcin.globetrotter.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home, container, false
            )

        val viewModel = createViewModel("Hello")

        binding.viewModel = viewModel

        viewModel.navigationActionEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val action = when (it) {
                    NavigationActions.BACK -> HomeFragmentDirections.back()
                }
                findNavController().navigate(action)
                viewModel.doneNavigation()
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun createViewModel(arg: String): HomeViewModel {
        val viewModelFactory = HomeViewModelFactory(arg)

        return ViewModelProviders.of(this, viewModelFactory)
            .get(HomeViewModel::class.java)

    }
}