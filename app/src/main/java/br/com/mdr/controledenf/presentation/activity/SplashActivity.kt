package br.com.mdr.controledenf.presentation.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.com.mdr.controledenf.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val delayTime = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handleSplash()
    }

    private fun handleSplash() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                goToActivity()
                finish()
            }, delayTime)
    }

    private fun goToActivity() {
        Firebase.auth.currentUser?.let {
            MainActivity.startMain(this)
        } ?: run {
            AuthActivity.startAuth(this)
        }
    }
}