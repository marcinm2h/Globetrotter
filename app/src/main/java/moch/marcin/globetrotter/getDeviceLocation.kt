package moch.marcin.globetrotter

import android.app.Activity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

fun getDeviceLocation(
    activity: Activity,
    onSuccess: (latLng: LatLng) -> Unit,
    onError: (e: Exception) -> Unit
) {
    try {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity);
        val locationResult = fusedLocationProviderClient.lastLocation

        locationResult.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val location = task.result
                if (location != null) {
                    onSuccess(LatLng(location.latitude, location.longitude))
                }
            }
        }
    } catch (e: SecurityException) {
        onError(e)
    }
}