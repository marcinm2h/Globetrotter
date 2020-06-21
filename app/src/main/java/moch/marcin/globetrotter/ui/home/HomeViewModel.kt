package moch.marcin.globetrotter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class NavigationActions {
    CREATE,
    SHOW_MAP,
    SHOW_DETAILS,
}

class HomeViewModel(arg: String) : ViewModel() {
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
