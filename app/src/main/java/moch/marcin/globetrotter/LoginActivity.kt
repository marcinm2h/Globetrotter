package moch.marcin.globetrotter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(), OnLogin {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onLogin() {
        MainActivity.loggedIn = true
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
