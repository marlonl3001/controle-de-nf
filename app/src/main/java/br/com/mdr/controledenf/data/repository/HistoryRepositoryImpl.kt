package br.com.mdr.controledenf.data.repository

import br.com.mdr.base.domain.Expense
import br.com.mdr.base.domain.Invoice
import br.com.mdr.controledenf.data.dao.ExpenseDAO
import br.com.mdr.controledenf.data.dao.InvoiceDAO
import br.com.mdr.controledenf.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val invoiceDAO: InvoiceDAO,
    private val expenseDAO: ExpenseDAO
) : HistoryRepository {
    override suspend fun saveExpense(expense: Expense) {
        expenseDAO.insert(expense)
    }

    override suspend fun saveInvoice(invoice: Invoice) {
        invoiceDAO.insert(invoice)
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDAO.delete(expense)
    }

    override suspend fun deleteInvoice(invoice: Invoice) {
        invoiceDAO.delete(invoice)
    }

    override suspend fun getExpenses() = expenseDAO.getExpenses()

    override suspend fun getInvoices() = invoiceDAO.getInvoices()

    override suspend fun getExpensesByYear(year: Int): List<Expense>? =
        expenseDAO.getExpenses()?.filter {it.year == year}

    override suspend fun getInvoicesByYear(year: Int): List<Invoice>? =
        invoiceDAO.getInvoices()?.filter {it.year == year}
}