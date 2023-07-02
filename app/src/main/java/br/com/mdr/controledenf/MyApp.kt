package br.com.mdr.controledenf

import android.app.Application
import br.com.mdr.controledenf.di.*
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApp: Application() {

    override fun onCreate() {
        setupFirebase()
        super.onCreate()

        setupKoin()
        setupTimber()
    }

    private fun setupFirebase() {
        FirebaseApp.initializeApp(this)
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    databaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}