package br.com.mdr.controledenf.data.repository

import br.com.mdr.base.domain.Company
import br.com.mdr.controledenf.data.dao.CompanyDAO
import br.com.mdr.controledenf.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val dao: CompanyDAO
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun saveCompany(company: Company): Company {
        val user = if (currentUser != null && currentUser?.email == company.email) {
            currentUser
        } else {
            signUpWithEmail(company.email, company.password)
        }
        user?.let {
            dao.insert(company.copy(key = it.uid))
            if (company.keepLogged.not()) {
                firebaseAuth.signOut()
            }
        }
        return company
    }

    override suspend fun loginWithEmail(email: String, password: String, keepLogged: Boolean): Company? {
        val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
        if (keepLogged.not()) {
            firebaseAuth.signOut()
        }
        return user?.toCompany()
    }

    override suspend fun getCompanyByEmail(email: String): Company? =
        dao.getCompanyByEmail(email)

    private suspend fun signUpWithEmail(email: String, password: String): FirebaseUser? =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await().user

    private suspend fun getCompany(user: FirebaseUser?): Company? =
        dao.getCompany(user?.uid)

    private suspend fun FirebaseUser.toCompany() =
        getCompany(this)

}