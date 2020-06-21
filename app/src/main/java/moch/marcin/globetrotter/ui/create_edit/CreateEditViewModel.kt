package moch.marcin.globetrotter.ui.create_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class NavigationActions {
    BACK_TO_DETAILS,
    BACK_TO_HOME
}

class CreateEditViewModel(arg: String) : ViewModel() {
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

    fun onBackToDetails() {
        _navigationActionEvent.value = NavigationActions.BACK_TO_DETAILS;
    }

    fun onBackToHome() {
        _navigationActionEvent.value = NavigationActions.BACK_TO_HOME;
    }
}
