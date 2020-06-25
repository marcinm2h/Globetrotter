package moch.marcin.globetrotter.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import moch.marcin.globetrotter.MapActivity
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

        binding.placesGrid.adapter = PlaceGridAdapter(PlaceGridAdapter.OnClickListener {
            findNavController().navigate(HomeFragmentDirections.showDetails(requireNotNull(it.id)))
        })

        viewModel.navigationActionEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it) {
                    NavigationActions.CREATE -> navigate(HomeFragmentDirections.create(null))
                    NavigationActions.SHOW_MAP -> redirectToMap()
                }
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun redirectToMap() {
        val intent = Intent(activity, MapActivity::class.java)
        startActivity(intent)
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
        viewModel.doneNavigation()
    }

    private fun createViewModel(): HomeViewModel {
        val viewModelFactory = HomeViewModelFactory()

        return ViewModelProviders.of(this, viewModelFactory)
            .get(HomeViewModel::class.java)

    }
}