package br.com.mdr.controledenf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mdr.base.domain.Expense
import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.base.domain.Invoice
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.base.presentation.BaseViewModel
import br.com.mdr.controledenf.domain.repository.HistoryRepository
import br.com.mdr.controledenf.domain.repository.PreferencesRepository

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
    private val preferencesRepository: PreferencesRepository
): BaseViewModel() {

    private val _expenseCategories = MutableLiveData<List<ExpenseCategory>>()
    var expenseCategories: LiveData<List<ExpenseCategory>> = _expenseCategories

    private val _expenses = MutableLiveData<List<Expense>>()
    var expenses: LiveData<List<Expense>> = _expenses

    private val _partnerCompanies = MutableLiveData<List<PartnerCompany>>()
    var partnerCompanies: LiveData<List<PartnerCompany>> = _partnerCompanies

    private val _invoices = MutableLiveData<List<Invoice>>()
    var invoices: LiveData<List<Invoice>> = _invoices

    var currentYear: Int = 0
        set(value) {
            field = value
            getExpenses()
            getInvoices()
        }

    fun getExpenseCategories() {
        launch {
            _expenseCategories.postValue(preferencesRepository.getExpenseCategories())
        }
    }

    fun getExpenses() {
        launch {
            _expenses.postValue(historyRepository.getExpenses())
        }
    }

    fun getPartnerCompanies() {
        launch {
            _partnerCompanies.postValue(preferencesRepository.getPartnerCompanies())
        }
    }

    fun getInvoices() {
        launch {
            _invoices.postValue(historyRepository.getInvoices())
        }
    }

    fun saveInvoice(invoice: Invoice) {
        launch(
            block = {
                historyRepository.saveInvoice(invoice)
            },
            successBlock = {
                getInvoices()
            }
        )
    }

    fun saveExpense(expense: Expense) {
        launch(
            block = {
                historyRepository.saveExpense(expense)
            },
            successBlock = {
                getExpenses()
            }
        )
    }

    fun deleteInvoice(invoice: Invoice) {
        launch(
            block = {
                historyRepository.deleteInvoice(invoice)
            },
            successBlock = {
                getInvoices()
            }
        )
    }

    fun deleteExpense(expense: Expense) {
        launch(
            block = {
                historyRepository.deleteExpense(expense)
            },
            successBlock = {
                getExpenses()
            }
        )
    }
}