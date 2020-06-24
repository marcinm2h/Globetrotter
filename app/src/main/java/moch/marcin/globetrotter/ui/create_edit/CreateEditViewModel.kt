package moch.marcin.globetrotter.ui.create_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moch.marcin.globetrotter.service.Api
import moch.marcin.globetrotter.service.Place
import moch.marcin.globetrotter.service.PlaceRequest

enum class NavigationActions {
    BACK_TO_DETAILS,
    BACK_TO_HOME
}

class CreateEditViewModel(private val placeId: String?) : ViewModel() {
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

    private val _editMode = MutableLiveData<Boolean>()

    val editMode: LiveData<Boolean>
        get() = _editMode


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
        if (placeId == null) {
            _editMode.value = false
            _place.value = Place(
                description = "",
                title = "",
                radius = 0,
                photo = null,
                id = null,
                ownerId = null
            )
        } else {
            _editMode.value = true
            getPlace(placeId)
        }
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

    private fun putPlace(placeId: String) {
        scope.launch {
            val place = requireNotNull(_place.value)
            val putPlaceDeferred = Api.placesService.putPlace(
                placeId,
                PlaceRequest(
                    title = place.title,
                    description = place.description,
                    radius = place.radius,
                    photo = place.photo
                )
            )
            try {
                _status.value = Status.LOADING
                val result = putPlaceDeferred.await()
                _place.value = result.data.place
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                _place.value = null
                _status.value = Status.ERROR
            }
        }
    }

    private fun postPlace() {
        scope.launch {
            val place = requireNotNull(_place.value)
            val postPlaceDeferred = Api.placesService.postPlace(
                PlaceRequest(
                    title = place.title,
                    description = place.description,
                    radius = place.radius,
                    photo = place.photo
                )
            )
            try {
                _status.value = Status.LOADING
                val result = postPlaceDeferred.await()
                _place.value = result.data.place
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                _place.value = null
                _status.value = Status.ERROR
            }
        }
    }

    fun onSubmit() {
        if (requireNotNull(_editMode.value)) {
            putPlace(requireNotNull(placeId))
        } else {
            postPlace()
        }
    }

    fun onBackToDetails() {
        _navigationActionEvent.value = NavigationActions.BACK_TO_DETAILS;
    }

    fun onBackToHome() {
        _navigationActionEvent.value = NavigationActions.BACK_TO_HOME;
    }
}
