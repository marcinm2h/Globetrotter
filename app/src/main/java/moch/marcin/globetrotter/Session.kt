package moch.marcin.globetrotter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

interface Actions {
    fun onLogin(activity: Activity)
}

class Session private constructor(ctx: Context, private val actions: Actions) {
    private val preferences: SharedPreferences = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE)
    var loggedIn: Boolean
        get() = preferences.getBoolean(PREFERENCES_KEY_LOGGED_IN, false)
        set(value) {
            with(preferences.edit()) {
                putBoolean(PREFERENCES_KEY_LOGGED_IN, value)
                commit()
            }
        }
    var token: String? = null

    fun login(activity: Activity) {
        loggedIn = true
        actions.onLogin(activity)
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

        const val PREFERENCES_KEY_LOGGED_IN = "Session.LOGGED_IN_d3323" // FIXME: name
    }
}
