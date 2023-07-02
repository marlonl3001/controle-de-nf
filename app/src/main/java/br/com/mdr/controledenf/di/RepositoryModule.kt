package br.com.mdr.controledenf.di

import br.com.mdr.controledenf.BuildConfig
import br.com.mdr.controledenf.data.repository.AuthRepositoryImpl
import br.com.mdr.controledenf.data.repository.HistoryRepositoryImpl
import br.com.mdr.controledenf.data.repository.PreferencesRepositoryImpl
import br.com.mdr.controledenf.domain.repository.AuthRepository
import br.com.mdr.controledenf.domain.repository.HistoryRepository
import br.com.mdr.controledenf.domain.repository.PreferencesRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get(), get()) }
    single<HistoryRepository> { HistoryRepositoryImpl(get(), get()) }

    // Firebase Auth
    single { FirebaseAuth.getInstance() }
    //GSO
    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }
    //GoogleSignInClient
    single { GoogleSignIn.getClient(androidContext(), get()) }
}