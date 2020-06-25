package moch.marcin.globetrotter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import moch.marcin.globetrotter.service.Place
import moch.marcin.globetrotter.service.Places
import moch.marcin.globetrotter.ui.home.HomeFragment


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    var places: List<Place>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activity_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bundle = intent.extras
        if (bundle != null) {
            val extra: Places? = bundle.getParcelable(HomeFragment.INTENT_EXTRA_KEY_PLACES)
            places = extra?.list
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation(map)
        if (places.isNullOrEmpty()) {
            selectLocation()
        } else {
            renderPlaces(places!!)
        }
    }

    private fun selectLocation() {
        showToast("Naciśnij dłużej aby wybrać punkt")
        setMapLongClick(map)
        if (isPermissionGranted()) {
            getDeviceLocation(this, onSuccess = {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 15f
                    )
                )
            }, onError = {
                showToast("Błąd")
            })
        }
    }

    private fun renderPlace(place: Place) {
        val latLng = LatLng(place.positionLat, place.positionLong)
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(place.title)
                .snippet(place.description)
        )
        val circle = map.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(place.radius.toDouble())
                .strokeColor(Color.argb(150, 255, 0, 0))
                .fillColor(Color.argb(30, 255, 0, 0))
        )
    }

    private fun renderPlaces(places: List<Place>) {
        places.forEach {
            renderPlace(it)
        }
        val first = places.first()
        val latLng = LatLng(first.positionLat, first.positionLong)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            finishWithResult(latLng)
        }
    }

    private fun enableMyLocation(map: GoogleMap) {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun isPermissionGranted(): Boolean =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation(map)
            }
        }
    }


    private fun finishWithResult(latLng: LatLng) {
        val returnIntent = Intent()
        returnIntent.putExtra(INTENT_EXTRA_KEY_POSITION_LAT, latLng.latitude)
        returnIntent.putExtra(INTENT_EXTRA_KEY_POSITION_LONG, latLng.longitude)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        const val INTENT_EXTRA_KEY_POSITION_LAT = "INTENT_EXTRA_KEY_POSITION_LAT"
        const val INTENT_EXTRA_KEY_POSITION_LONG = "INTENT_EXTRA_KEY_POSITION_LONG"
    }
}