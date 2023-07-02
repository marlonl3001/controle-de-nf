package br.com.mdr.controledenf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.base.presentation.BaseViewModel
import br.com.mdr.controledenf.R
import br.com.mdr.controledenf.domain.repository.PreferencesRepository

class PreferencesViewModel(private val repository: PreferencesRepository): BaseViewModel() {

    private val _companies = MutableLiveData<List<PartnerCompany>>()
    var companies: LiveData<List<PartnerCompany>> = _companies

    private val _categories = MutableLiveData<List<ExpenseCategory>>()
    var categories: LiveData<List<ExpenseCategory>> = _categories

    fun savePartnerCompany(company: PartnerCompany) {
        launch(
            block = {
                repository.savePartnerCompany(company)
            },
            successBlock = {
                getPartnerCompanies()
                _successMessage.postValue(R.string.register_success)
            }
        )
    }

    fun saveCategory(category: ExpenseCategory) {
        launch(
            block = {
                repository.saveExpenseCategory(category)
            },
            successBlock = {
                getExpenseCategories()
                _successMessage.postValue(R.string.register_success)
            }
        )
    }

    fun getPartnerCompanies() {
        launch {
            _companies.postValue(repository.getPartnerCompanies())
        }
    }

    fun getExpenseCategories() {
        launch {
            _categories.postValue(repository.getExpenseCategories())
        }
    }
}