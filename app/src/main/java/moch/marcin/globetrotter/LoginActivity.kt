package moch.marcin.globetrotter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.instance.currentActivity = this
        setContentView(R.layout.activity_login)
    }
}
