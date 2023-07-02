package br.com.mdr.controledenf.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import br.com.mdr.controledenf.R
import br.com.mdr.controledenf.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        fun startMain(context: Context) {
            ContextCompat.startActivity(context, Intent(context, MainActivity::class.java), null)
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.navController)
    }
}