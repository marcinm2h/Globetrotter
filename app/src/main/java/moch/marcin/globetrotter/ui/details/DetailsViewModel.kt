package moch.marcin.globetrotter.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class NavigationActions {
    BACK,
//    EDIT
}

class DetailsViewModel(arg: String) : ViewModel() {
    val state = MutableLiveData<String>()

    private val _navigationActionEvent = MutableLiveData<NavigationActions?>()

    val navigationActionEvent: LiveData<NavigationActions?>
        get() = _navigationActionEvent

    fun doneNavigation() {
        _navigationActionEvent.value = null
    }


    init {
        state.value = arg
    }

    fun onBack() {
        _navigationActionEvent.value = NavigationActions.BACK;
    }
}
