package moch.marcin.globetrotter.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel(arg: String) : ViewModel() {
    val state = MutableLiveData<String>()

    init {
        state.value = arg
    }
}
