package br.com.mdr.controledenf.data.dao

import androidx.room.*
import br.com.mdr.base.domain.Company

@Dao
interface CompanyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: Company?)

    @Query("SELECT * FROM Company WHERE `key` == :key")
    suspend fun getCompany(key: String?): Company?

    @Query("SELECT * FROM Company WHERE `email` == :email")
    suspend fun getCompanyByEmail(email: String): Company?

    @Update
    suspend fun update(company: Company)
}