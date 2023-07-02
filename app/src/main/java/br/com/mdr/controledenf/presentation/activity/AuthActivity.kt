package br.com.mdr.controledenf.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import br.com.mdr.controledenf.R

class AuthActivity : AppCompatActivity() {
    companion object {
        fun startAuth(context: Context) {
            ContextCompat.startActivity(context, Intent(context, AuthActivity::class.java), null)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}