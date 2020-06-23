package moch.marcin.globetrotter.ui.home

import android.os.Bundle
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import moch.marcin.globetrotter.R
import moch.marcin.globetrotter.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home, container, false
            )

        viewModel = createViewModel()

        binding.viewModel = viewModel

        viewModel.navigationActionEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val action = when (it) {
                    NavigationActions.CREATE -> HomeFragmentDirections.create()
                    NavigationActions.SHOW_DETAILS -> HomeFragmentDirections.showDetails()
                    NavigationActions.SHOW_MAP -> HomeFragmentDirections.showMap()
                }
                findNavController().navigate(action)
                viewModel.doneNavigation()
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun createViewModel(): HomeViewModel {
        val viewModelFactory = HomeViewModelFactory()

        return ViewModelProviders.of(this, viewModelFactory)
            .get(HomeViewModel::class.java)

    }
}