package moch.marcin.globetrotter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moch.marcin.globetrotter.service.Api

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
        getPlaces()
    }

    private fun getPlaces() {
        scope.launch {
            var getPlacesDeferred = Api.placesService.getPlaces()
            try {
                _places.value = "LOADING"
                val result = getPlacesDeferred.await()
                _places.value = result
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
}
