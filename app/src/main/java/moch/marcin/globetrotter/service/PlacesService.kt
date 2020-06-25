package moch.marcin.globetrotter.service;

import kotlinx.coroutines.Deferred
import retrofit2.http.*

data class Place(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val radius: Int,
    val photo: String?,
    val positionLat: Double,
    val positionLong: Double
)

data class PlaceResponse(
    val place: Place
)

data class PlacesResponse(
    val places: List<Place>
)

data class PlaceRequest(
    val title: String,
    val description: String,
    val radius: Int,
    val photo: String? = null,
    val positionLat: Double,
    val positionLong: Double
)

interface PlacesService {
    @GET("places")
    fun getPlaces(): Deferred<Response<PlacesResponse>>

    @GET("places/{placeId}")
    fun getPlace(@Path("placeId") placeId: String): Deferred<Response<PlaceResponse>>

    @DELETE("places/{placeId}")
    fun deletePlace(@Path("placeId") placeId: String): Deferred<Response<PlaceResponse>>

    @PUT("places/{placeId}")
    fun putPlace(@Path("placeId") placeId: String, @Body body: PlaceRequest): Deferred<Response<PlaceResponse>>

    @POST("places")
    fun postPlace(@Body body: PlaceRequest): Deferred<Response<PlaceResponse>>
}
