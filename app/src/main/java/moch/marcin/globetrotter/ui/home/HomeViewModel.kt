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
import moch.marcin.globetrotter.service.Place
import moch.marcin.globetrotter.service.PlaceRequest

enum class NavigationActions {
    CREATE,
    SHOW_MAP
}

class HomeViewModel() : ViewModel() {
    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _places = MutableLiveData<List<Place>>()

    val places: LiveData<List<Place>>
        get() = _places

    private val _message = MutableLiveData<String>()

    val message: LiveData<String>
        get() = _message

    private val _navigationActionEvent = MutableLiveData<NavigationActions?>()

    val navigationActionEvent: LiveData<NavigationActions?>
        get() = _navigationActionEvent

    fun doneNavigation() {
        _navigationActionEvent.value = null
    }

    init {
        getPlaces()
//        getPlace("idtgkKgGSH")
//        putPlace("idtgkKgGSH")
//        postPlace()
    }

    private fun getPlaces() {
        scope.launch {
            val getPlacesDeferred = Api.placesService.getPlaces()
            try {
                _message.value = "LOADING"
                val result = getPlacesDeferred.await()
                if (result.data.places.isNotEmpty()) {
                    _places.value = result.data.places
                } else {
                    _places.value = ArrayList()
                }
            } catch (e: Exception) {
                _message.value = "ERROR"
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
                _message.value = "LOADING"
                val result = putPlaceDeferred.await()
                _message.value = result.data.place.toString()
            } catch (e: Exception) {
                _message.value = "ERROR"
            }
        }
    }

    private fun deletePlace(placeId: String) {
        scope.launch {
            val deletePlaceDeferred = Api.placesService.deletePlace(placeId)
            try {
                _message.value = "LOADING"
                val result = deletePlaceDeferred.await()
                _message.value = result.data.place.toString()
            } catch (e: Exception) {
                _message.value = "ERROR"
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
                _message.value = "LOADING"
                val result = postPlaceDeferred.await()
                _message.value = result.data.place.toString()
            } catch (e: Exception) {
                _message.value = "ERROR"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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
