package moch.marcin.globetrotter.ui.create_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moch.marcin.globetrotter.service.Api
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


    val currentLocation = MutableLiveData<Boolean>()

    var location: String? = null

    val description = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    val photo = MutableLiveData<String?>()

    val radius = MutableLiveData<String>()

    private val _navigationActionEvent = MutableLiveData<NavigationActions?>()

    val navigationActionEvent: LiveData<NavigationActions?>
        get() = _navigationActionEvent

    fun doneNavigation() {
        _navigationActionEvent.value = null
    }


    init {
        if (placeId == null) {
            _editMode.value = false
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
                val place = result.data.place
                description.value = place.description
                title.value = place.title
                photo.value = place.photo
                radius.value = place.radius.toString()

                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }

    private fun putPlace(placeId: String, onSuccess: () -> Unit) {
        scope.launch {
            val putPlaceDeferred = Api.placesService.putPlace(
                placeId,
                PlaceRequest(
                    title = requireNotNull(title.value),
                    description = requireNotNull(description.value),
                    radius = requireNotNull(radius.value).toInt(),
                    photo = photo.value
                )
            )
            try {
                _status.value = Status.LOADING
                putPlaceDeferred.await()
                _status.value = Status.SUCCESS
                onSuccess()
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }

    private fun postPlace(onSuccess: () -> Unit) {
        scope.launch {
            val postPlaceDeferred = Api.placesService.postPlace(
                PlaceRequest(
                    title = requireNotNull(title.value),
                    description = requireNotNull(description.value),
                    radius = requireNotNull(radius.value).toInt(),
                    photo = photo.value
                )
            )
            try {
                _status.value = Status.LOADING
                postPlaceDeferred.await()
                _status.value = Status.SUCCESS
                onSuccess()
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }

    private fun deletePlace(onSuccess: () -> Unit) {
        scope.launch {
            val deletePlaceDeferred = Api.placesService.deletePlace(
                requireNotNull(placeId)
            )
            try {
                _status.value = Status.LOADING
                deletePlaceDeferred.await()
                _status.value = Status.SUCCESS
                onSuccess()
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }

    fun onSubmit() {
        if (requireNotNull(_editMode.value)) {
            putPlace(requireNotNull(placeId)) {
                onBackToDetails()
            }
        } else {
            postPlace {
                onBackToHome()
            }
        }
    }

    fun onDelete() {
        deletePlace {
            onBackToHome()
        }
    }

    fun onBackToDetails() {
        _navigationActionEvent.value = NavigationActions.BACK_TO_DETAILS;
    }

    fun onBackToHome() {
        _navigationActionEvent.value = NavigationActions.BACK_TO_HOME;
    }

    fun toggleLocation() {
        currentLocation.value = currentLocation.value != true
    }
}
