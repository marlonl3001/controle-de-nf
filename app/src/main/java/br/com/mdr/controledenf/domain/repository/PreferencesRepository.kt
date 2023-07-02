package br.com.mdr.controledenf.domain.repository

import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.base.domain.PartnerCompany

interface PreferencesRepository {
    suspend fun savePartnerCompany(company: PartnerCompany)
    suspend fun getPartnerCompanies(): List<PartnerCompany>?
    suspend fun saveExpenseCategory(category: ExpenseCategory)
    suspend fun getExpenseCategories(): List<ExpenseCategory>?
}