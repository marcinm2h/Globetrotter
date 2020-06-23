package moch.marcin.globetrotter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

interface Actions {
    fun onLogin(activity: Activity)
    fun onLogout(activity: Activity)
}

class Session private constructor(ctx: Context, private val actions: Actions) {
    private val preferences: SharedPreferences = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE)
    lateinit var currentActivity: Activity
    var loggedIn: Boolean
        get() = preferences.getBoolean(PREFERENCES_KEY_TOKEN, false)
        set(value) {
            with(preferences.edit()) {
                putBoolean(PREFERENCES_KEY_TOKEN, value)
                commit()
            }
        }
    var token: String?
        get() = preferences.getString(PREFERENCES_KEY_LOGGED_IN, null)
        set(value) {
            with(preferences.edit()) {
                putString(PREFERENCES_KEY_LOGGED_IN, value)
                commit()
            }
        }

    fun login(token: String) {
        this.token = token
        loggedIn = true
        actions.onLogin(currentActivity)
    }

    fun logout() {
        this.token = null
        loggedIn = false
        actions.onLogout(currentActivity)
    }

    companion object {
        private var HOLDER: Session? = null
        val instance: Session
            get() = requireNotNull(HOLDER)

        fun create(ctx: Context, actions: Actions): Session {
            if (HOLDER == null) {
                HOLDER = Session(ctx, actions)
            }
            return instance
        }

        const val PREFERENCES_KEY_LOGGED_IN = "Session.LOGGED_IN"
        const val PREFERENCES_KEY_TOKEN = "Session.TOKEN"
    }
}
