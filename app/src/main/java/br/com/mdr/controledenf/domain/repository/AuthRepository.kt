package br.com.mdr.controledenf.domain.repository

import br.com.mdr.base.domain.Company
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun loginWithEmail(email: String, password: String, keepLogged: Boolean = false): Company?
    suspend fun saveCompany(company: Company): Company
    suspend fun getCompanyByEmail(email: String): Company?
}