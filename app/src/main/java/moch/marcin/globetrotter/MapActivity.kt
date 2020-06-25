package moch.marcin.globetrotter

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
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
import java.util.*


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
        val extra: Places? = bundle!!.getParcelable(HomeFragment.INTENT_EXTRA_KEY_PLACES)
        places = extra?.list
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
//        setMapLongClick(mMap)
////        mMap.setOnMarkerClickListener {
////            false
////        }
        enableMyLocation(map)
        val list = if (places.isNullOrEmpty()) return else places!!
        list.forEach {
            renderPlace(it)
        }
        val first = list.first()
        val latLng = LatLng(first.positionLat, first.positionLong)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Aktualne miejsce")
                    .snippet(snippet)
            )
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
        grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation(map)
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
