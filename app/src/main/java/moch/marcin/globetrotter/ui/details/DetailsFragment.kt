package moch.marcin.globetrotter.ui.details

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
import moch.marcin.globetrotter.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDetailsBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_details, container, false
            )

        val viewModel = createViewModel("Hello")

        binding.viewModel = viewModel

        viewModel.navigationActionEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val action = when (it) {
                    NavigationActions.BACK -> DetailsFragmentDirections.back()
//                    NavigationActions.EDIT -> DetailsFragmentDirections.edit()
                }
                findNavController().navigate(action)
                viewModel.doneNavigation()
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun createViewModel(arg: String): DetailsViewModel {
        val viewModelFactory = DetailsViewModelFactory(arg)

        return ViewModelProviders.of(this, viewModelFactory)
            .get(DetailsViewModel::class.java)

    }
}