package br.com.mdr.controledenf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mdr.base.domain.Company
import br.com.mdr.base.presentation.BaseViewModel
import br.com.mdr.controledenf.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel() {

    private val _company = MutableLiveData<Company?>()
    var company: LiveData<Company?> = _company

    private val _goToStepTwo = MutableLiveData(false)
    var goToStepTwo: LiveData<Boolean> = _goToStepTwo

    init {
        FirebaseAuth.getInstance().currentUser?.email?.let {
            verifyUserWithEmail(it)
        }
    }

    fun loginWithEmail(email: String, password: String, keepLogged: Boolean = false) {
        launch {
            _company.postValue(repository.loginWithEmail(email, password, keepLogged))
        }
    }

    fun verifyUserWithEmail(email: String) {
        launch {
            val c = repository.getCompanyByEmail(email)
            c?.let { _company.postValue(it) }
            _goToStepTwo.postValue(c == null)
        }
    }

    fun saveCompany(email: String, name: String, document: String, companyName: String,
        phone: String, password: String, keepLogged: Boolean, billingThreshold: Float) {
        val company = Company(
            email = email,
            name = name,
            document = document,
            companyName = companyName,
            phone = phone,
            password = password,
            keepLogged = keepLogged,
            billingThreshold = billingThreshold
        )
        launch {
            _company.postValue(repository.saveCompany(company))
        }
    }
}