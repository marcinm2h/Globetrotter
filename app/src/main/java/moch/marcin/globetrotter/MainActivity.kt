package moch.marcin.globetrotter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = Session.create(applicationContext, object : Actions {
            override fun onLogin(activity: Activity) {
                val intent = Intent(activity, MainActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }

            override fun onLogout(activity: Activity) {
                val intent = Intent(activity, MainActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        })
        session.currentActivity = this

        if (!session.loggedIn) {
            return redirectToLogin()
        }

        setContentView(R.layout.activity_main)
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> {
            Session.instance.logout()
            true
        }
        else -> true
    }

    fun showToast(message: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, message, duration)
        toast.show()
    }
}
