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

    val description = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    val photo = MutableLiveData<String?>()

    val radius = MutableLiveData<String>()

    val positionLat = MutableLiveData<Double>()

    val positionLong = MutableLiveData<Double>()

    private val _navigationActionEvent = MutableLiveData<NavigationActions?>()

    val navigationActionEvent: LiveData<NavigationActions?>
        get() = _navigationActionEvent

    fun doneNavigation() {
        _navigationActionEvent.value = null
    }

    private val _photoChangeEvent = MutableLiveData<Boolean>()

    val photoChangeEvent: LiveData<Boolean>
        get() = _photoChangeEvent

    fun donePhotoChange() {
        _photoChangeEvent.value = null
    }

    private val _validationErrorEvent = MutableLiveData<Boolean>()

    val validationErrorEvent: LiveData<Boolean>
        get() = _validationErrorEvent

    fun doneValidationError() {
        _validationErrorEvent.value = null
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

    private fun putPlace(placeId: String, requestBody: PlaceRequest, onSuccess: () -> Unit) {
        scope.launch {
            val putPlaceDeferred = Api.placesService.putPlace(
                placeId,
                requestBody
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

    private fun postPlace(requestBody: PlaceRequest, onSuccess: () -> Unit) {
        scope.launch {
            val postPlaceDeferred = Api.placesService.postPlace(
                requestBody
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

    private fun deletePlace(placeId: String, onSuccess: () -> Unit) {
        scope.launch {
            val deletePlaceDeferred = Api.placesService.deletePlace(
                placeId
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

    fun isValid(): Boolean {
        if (title.value == null) {
            return false
        }
        if (description.value == null) {
            return false
        }
        if (radius.value == null) {
            return false
        }
        return  true
    }

    fun onSubmit() {
        val editMode = _editMode.value ?: return
        if (!isValid()) {
            _validationErrorEvent.value = true
            return
        }

        val requestBody = PlaceRequest(
            title = requireNotNull(title.value),
            description = requireNotNull(description.value),
            radius = requireNotNull(radius.value).toInt(),
            photo = photo.value,
            positionLat = positionLat.value ?: 37.422160, // FIXME: currposition
            positionLong = positionLong.value ?: -122.084270 // FIXME: currposition
        )

        if (editMode) {
            putPlace(requireNotNull(placeId), requestBody) {
                onBackToDetails()
            }
        } else {
            postPlace(requestBody) {
                onBackToHome()
            }
        }
    }

    fun onDelete() {
        val placeId = placeId ?: return
        deletePlace(placeId) {
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

    fun onPhotoChange() {
        _photoChangeEvent.value = true
    }

    fun onLongClick(): Boolean {
        onPhotoRemove()
        return true
    }

    private fun onPhotoRemove() {
        photo.value = null
    }
}
