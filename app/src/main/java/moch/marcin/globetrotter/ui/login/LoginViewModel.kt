package moch.marcin.globetrotter.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import moch.marcin.globetrotter.service.Api
import moch.marcin.globetrotter.service.LoginRequest

class LoginViewModel() : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _loginEvent = MutableLiveData<Boolean?>()

    val loginEvent: LiveData<Boolean?>
        get() = _loginEvent

    fun doneLogin() {
        _loginEvent.value = null
    }

    fun onLogin() {
        _loginEvent.value = true;
    }

    private val _signInEvent = MutableLiveData<Boolean?>()

    val signInEvent: LiveData<Boolean?>
        get() = _signInEvent

    fun doneSignIn() {
        _signInEvent.value = null
    }

    fun onSignIn() {
        _signInEvent.value = true;
    }

    fun auth(userId: String) {
        coroutineScope.launch {
            val responseDeferred = Api.authService.loginAsync(LoginRequest(userId))
            try {
                val response = responseDeferred.await()
                println(response.data.token)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}
