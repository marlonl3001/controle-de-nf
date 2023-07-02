package br.com.mdr.controledenf.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.mdr.base.domain.Company
import br.com.mdr.base.domain.Expense
import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.base.domain.Invoice
import br.com.mdr.base.domain.PartnerCompany

@Database(
    entities = [
        Company::class,
        PartnerCompany::class,
        ExpenseCategory::class,
        Invoice::class,
        Expense::class
    ],
    version = 1,
    exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun getCompanyDAO(): CompanyDAO
    abstract fun getPartnerCompanyDAO(): PartnerCompanyDAO
    abstract fun getExpenseCategoryDAO(): ExpenseCategoryDAO
    abstract fun getExpenseDAO(): ExpenseDAO
    abstract fun getInvoiceDAO(): InvoiceDAO
}