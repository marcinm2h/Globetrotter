package moch.marcin.globetrotter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moch.marcin.globetrotter.Session
import moch.marcin.globetrotter.service.Api
import moch.marcin.globetrotter.service.PlaceRequest

enum class NavigationActions {
    CREATE,
    SHOW_MAP,
    SHOW_DETAILS,
}

class HomeViewModel() : ViewModel() {
    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _places = MutableLiveData<String>()

    val places: LiveData<String>
        get() = _places

    private val _navigationActionEvent = MutableLiveData<NavigationActions?>()

    val navigationActionEvent: LiveData<NavigationActions?>
        get() = _navigationActionEvent

    fun doneNavigation() {
        _navigationActionEvent.value = null
    }

    init {
//        getPlaces()
//        getPlace("idtgkKgGSH")
//        putPlace("idtgkKgGSH")
        postPlace()
    }

    private fun getPlaces() {
        scope.launch {
            val getPlacesDeferred = Api.placesService.getPlaces()
            try {
                _places.value = "LOADING"
                val result = getPlacesDeferred.await()
                if (result.data.places.size > 0) {
                    _places.value = result.data.places[0].toString()
                } else {
                    _places.value = "Pusta tablica"
                }
            } catch (e: Exception) {
                _places.value = "ERROR"
            }
        }
    }

    private fun getPlace(placeId: String) {
        scope.launch {
            val getPlacesDeferred = Api.placesService.getPlace(placeId)
            try {
                _places.value = "LOADING"
                val result = getPlacesDeferred.await()
                _places.value = result.data.place.toString()
            } catch (e: Exception) {
                _places.value = "ERROR"
            }
        }
    }

    private fun putPlace(placeId: String) {
        scope.launch {
            val putPlaceDeferred = Api.placesService.putPlace(placeId, PlaceRequest(
                description = "dupa",
                photo = "photo",
                radius = 2,
                title = "dupa"
            ))
            try {
                _places.value = "LOADING"
                val result = putPlaceDeferred.await()
                _places.value = result.data.place.toString()
            } catch (e: Exception) {
                _places.value = "ERROR"
            }
        }
    }

    private fun deletePlace(placeId: String) {
        scope.launch {
            val deletePlaceDeferred = Api.placesService.deletePlace(placeId)
            try {
                _places.value = "LOADING"
                val result = deletePlaceDeferred.await()
                _places.value = result.data.place.toString()
            } catch (e: Exception) {
                _places.value = "ERROR"
            }
        }
    }

    private fun postPlace() {
        scope.launch {
            val postPlaceDeferred = Api.placesService.postPlace(PlaceRequest(
                description = "dupa",
                photo = "photo",
                radius = 2,
                title = "dupa"
            ))
            try {
                _places.value = "LOADING"
                val result = postPlaceDeferred.await()
                _places.value = result.data.place.toString()
            } catch (e: Exception) {
                _places.value = "ERROR"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onShowDetails() {
        _navigationActionEvent.value = NavigationActions.SHOW_DETAILS
    }

    fun onShowMap() {
        _navigationActionEvent.value = NavigationActions.SHOW_MAP
    }

    fun onCreate() {
        _navigationActionEvent.value = NavigationActions.CREATE
    }

    fun onLogOut() {
        Session.instance.logout()
    }
}
