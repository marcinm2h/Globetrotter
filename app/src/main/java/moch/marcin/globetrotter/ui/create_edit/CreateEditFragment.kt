package moch.marcin.globetrotter.ui.create_edit

import android.app.Activity
import android.content.Intent
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
import moch.marcin.globetrotter.CameraActivity
import moch.marcin.globetrotter.R
import moch.marcin.globetrotter.databinding.FragmentCreateEditBinding


class CreateEditFragment : Fragment() {
    private val args: CreateEditFragmentArgs by navArgs()
    private var onPhotoResult: (photoBase64: String) -> Unit = {
    }

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

        viewModel.photoChangeEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                openCameraActivity { photo ->
                    viewModel.photo.value = photo
                }
            }
        })

        viewModel.validationErrorEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToast(R.string.validation_error)
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun showToast(message: Int) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), requireContext().getString(message), duration)
        toast.show()
    }

    private fun createViewModel(placeId: String?): CreateEditViewModel {
        val viewModelFactory = CreateEditViewModelFactory(placeId)

        return ViewModelProviders.of(this, viewModelFactory)
            .get(CreateEditViewModel::class.java)

    }

    private fun openCameraActivity(onSuccess: (photoBase64: String) -> Unit) {
        onPhotoResult = onSuccess
        val intent = Intent(activity, CameraActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra(CameraActivity.INTENT_EXTRA_KEY_RESULT)
                if (result != null) {
                    onPhotoResult(result)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 99
    }
}