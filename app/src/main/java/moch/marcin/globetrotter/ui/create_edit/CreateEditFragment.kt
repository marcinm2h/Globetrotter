package moch.marcin.globetrotter.ui.create_edit

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import moch.marcin.globetrotter.R
import moch.marcin.globetrotter.databinding.FragmentCreateEditBinding

class CreateEditFragment : Fragment() {
    private val args: CreateEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCreateEditBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create_edit, container, false
            )

        val viewModel = createViewModel(args.placeId)

        binding.viewModel = viewModel

        viewModel.navigationActionEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val action = when (it) {
                    NavigationActions.BACK_TO_DETAILS -> CreateEditFragmentDirections.backToDetails(
                        requireNotNull(args.placeId)
                    )
                    NavigationActions.BACK_TO_HOME -> CreateEditFragmentDirections.backToHome()
                }
                findNavController().navigate(action)
                viewModel.doneNavigation()
            }
        })

        viewModel.currentLocation.observe(viewLifecycleOwner, Observer {
            if (it != null && it) {
                showToast(R.string.location_message)
            }
        })

        binding.setLifecycleOwner(this)
//
//        val imageCapture = ImageCapture.Builder()
//            .setTargetRotation(view.display.rotation)
//            .build()
//
//        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageCapture,
//            imageAnalysis, preview)

        return binding.root
    }

    fun showToast(message: Int) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), requireContext().getString(message), duration)
        toast.show()
    }

    private fun createViewModel(placeId: String?): CreateEditViewModel {
        val viewModelFactory = CreateEditViewModelFactory(placeId)

        return ViewModelProviders.of(this, viewModelFactory)
            .get(CreateEditViewModel::class.java)

    }
}