package br.com.mdr.controledenf.domain.repository

import br.com.mdr.base.domain.Expense
import br.com.mdr.base.domain.Invoice

interface HistoryRepository {
    suspend fun saveExpense(expense: Expense)
    suspend fun saveInvoice(invoice: Invoice)
    suspend fun getExpenses(): List<Expense>?
    suspend fun getInvoices(): List<Invoice>?
    suspend fun deleteExpense(expense: Expense)
    suspend fun deleteInvoice(invoice: Invoice)
    suspend fun getExpensesByYear(year: Int): List<Expense>?
    suspend fun getInvoicesByYear(year: Int): List<Invoice>?
}