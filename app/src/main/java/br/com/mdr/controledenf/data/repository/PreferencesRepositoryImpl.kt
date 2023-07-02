package br.com.mdr.controledenf.data.repository

import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.controledenf.data.dao.ExpenseCategoryDAO
import br.com.mdr.controledenf.data.dao.PartnerCompanyDAO
import br.com.mdr.controledenf.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(
    private val partnerDao: PartnerCompanyDAO,
    private val categoryDAO: ExpenseCategoryDAO
) : PreferencesRepository {

    override suspend fun savePartnerCompany(company: PartnerCompany) {
        partnerDao.insert(company)
    }

    override suspend fun getPartnerCompanies(): List<PartnerCompany>? =
        partnerDao.getCompanies()

    override suspend fun saveExpenseCategory(category: ExpenseCategory) {
        categoryDAO.insert(category)
    }

    override suspend fun getExpenseCategories(): List<ExpenseCategory>? =
        categoryDAO.getCategories()
}