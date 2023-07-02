package br.com.mdr.controledenf.di

import br.com.mdr.controledenf.presentation.viewmodel.AuthViewModel
import br.com.mdr.controledenf.presentation.viewmodel.HistoryViewModel
import br.com.mdr.controledenf.presentation.viewmodel.PreferencesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { PreferencesViewModel(get()) }
    viewModel { HistoryViewModel(get(), get()) }
}