package moch.marcin.globetrotter

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import moch.marcin.globetrotter.service.Place
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activity_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun renderPlace(place: Place) {
        val latLng = LatLng(place.positionLat, place.positionLong)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(place.title)
                .snippet(place.description)
        )
        val circle = mMap.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(place.radius.toDouble())
                .strokeColor(Color.argb(150, 255, 0, 0))
                .fillColor(Color.argb(30, 255, 0, 0))
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latitude = 37.422160
        val longitude = -122.084270
        val zoomLevel = 15f

        setMapLongClick(mMap)

        mMap.setOnMarkerClickListener {
            false
        }


        val latitude2 = 37.5
        val longitude2 = -122.2

        val homeLatLng = LatLng(latitude, longitude)
        val homeLatLng2 = LatLng(latitude2, longitude2)


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        mMap.addMarker(MarkerOptions().position(homeLatLng).title("NEW MARKER"))
//        mMap.addMarker(MarkerOptions().position(homeLatLng2).title("NEW MARKER 2"))


        val circle = mMap.addCircle(
            CircleOptions()
                .center(homeLatLng)
                .radius(300.0)
                .strokeColor(Color.argb(150, 255, 0, 0))
                .fillColor(Color.argb(30, 255, 0, 0))
        )
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
}
