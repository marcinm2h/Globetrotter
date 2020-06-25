package moch.marcin.globetrotter.ui.create_edit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import moch.marcin.globetrotter.CameraActivity
import moch.marcin.globetrotter.MapActivity
import moch.marcin.globetrotter.R
import moch.marcin.globetrotter.databinding.FragmentCreateEditBinding
import moch.marcin.globetrotter.getDeviceLocation
import java.util.*


class CreateEditFragment : Fragment() {
    private val args: CreateEditFragmentArgs by navArgs()
    private lateinit var viewModel: CreateEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCreateEditBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create_edit, container, false
            )

        viewModel = createViewModel(args.placeId)

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
                openCameraActivity()
            }
        })

        viewModel.validationErrorEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToast(R.string.validation_error)
            }
        })

        binding.map.setOnClickListener {
            openMapActivity()
        }

        binding.location.setOnClickListener{
            if (isPermissionGranted()) {
                getLocation()
            } else {
                requestPermissions()
            }
        }

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun showToast(message: Int, vararg args: Any) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), requireContext().getString(message, *args), duration)
        toast.show()
    }

    private fun createViewModel(placeId: String?): CreateEditViewModel {
        val viewModelFactory = CreateEditViewModelFactory(placeId)

        return ViewModelProviders.of(this, viewModelFactory)
            .get(CreateEditViewModel::class.java)

    }

    private fun openMapActivity() {
        val intent = Intent(activity, MapActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_MAP)
    }

    private fun openCameraActivity() {
        val intent = Intent(activity, CameraActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra(CameraActivity.INTENT_EXTRA_KEY_RESULT)
                if (result != null) {
                    viewModel.photo.value = result
                }
            }
        }
        if (requestCode == REQUEST_CODE_MAP) {
            if (resultCode == Activity.RESULT_OK) {
                val positionLat =
                    data?.getDoubleExtra(MapActivity.INTENT_EXTRA_KEY_POSITION_LAT, 1.0)
                val positionLong =
                    data?.getDoubleExtra(MapActivity.INTENT_EXTRA_KEY_POSITION_LONG, 1.0)
                if (positionLat != null && positionLong != null) {
                    viewModel.positionLat.value = positionLat
                    viewModel.positionLong.value = positionLong
                }
            }
        }
    }

    private fun getLocation() {
        getDeviceLocation(activity as Activity, onSuccess = {
            viewModel.positionLong.value = it.longitude
            viewModel.positionLat.value = it.latitude
            showToast(R.string.position_set, it.latitude, it.longitude)
        }, onError = {
            showToast(R.string.error)
        })
    }



    private fun isPermissionGranted(): Boolean =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                activity?.baseContext!!, it
            ) == PackageManager.PERMISSION_GRANTED
        }


    private fun requestPermissions() {
        if (isPermissionGranted()) {
            getLocation()
        } else {
            ActivityCompat.requestPermissions(
                activity as Activity,
                REQUIRED_PERMISSIONS,
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 3
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_CAMERA = 1
        private const val REQUEST_CODE_MAP = 2
    }
}