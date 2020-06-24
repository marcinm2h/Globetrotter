package moch.marcin.globetrotter.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moch.marcin.globetrotter.service.Api
import moch.marcin.globetrotter.service.Place

enum class NavigationActions {
    BACK,
    EDIT
}

class DetailsViewModel(private val placeId: String) : ViewModel() {
    enum class Status {
        LOADING,
        ERROR,
        SUCCESS
    }

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<Status>()

    val status: LiveData<Status>
        get() = _status

    private val _place = MutableLiveData<Place>()

    val place: LiveData<Place>
        get() = _place

    private val _navigationActionEvent = MutableLiveData<NavigationActions?>()

    val navigationActionEvent: LiveData<NavigationActions?>
        get() = _navigationActionEvent

    fun doneNavigation() {
        _navigationActionEvent.value = null
    }

    init {
        getPlace(placeId)
    }

    private fun getPlace(placeId: String) {
        scope.launch {
            val getPlaceDeferred = Api.placesService.getPlace(placeId)
            try {
                _status.value = Status.LOADING
                val result = getPlaceDeferred.await()
                _place.value = result.data.place
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                _place.value = null
                _status.value = Status.ERROR
            }
        }
    }

    fun onBack() {
        _navigationActionEvent.value = NavigationActions.BACK;
    }

    fun onEdit() {
        _navigationActionEvent.value = NavigationActions.EDIT;
    }
}
