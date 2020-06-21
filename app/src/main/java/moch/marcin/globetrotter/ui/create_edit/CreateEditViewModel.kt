package moch.marcin.globetrotter.ui.create_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class NavigationActions {
    BACK
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

    fun onBack() {
        _navigationActionEvent.value = NavigationActions.BACK;
    }
}
