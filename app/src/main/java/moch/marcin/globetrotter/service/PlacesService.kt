package moch.marcin.globetrotter.service;

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface PlacesService {
    @GET("places")
    fun getPlaces(): Deferred<String>
}
