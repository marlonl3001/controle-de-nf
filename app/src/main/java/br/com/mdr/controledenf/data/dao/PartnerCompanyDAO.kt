package br.com.mdr.controledenf.data.dao

import androidx.room.*
import br.com.mdr.base.domain.PartnerCompany

@Dao
interface PartnerCompanyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: PartnerCompany?)

    @Query("SELECT * FROM PartnerCompany ORDER BY id")
    suspend fun getCompanies(): List<PartnerCompany>?

    @Query("SELECT * FROM PartnerCompany WHERE `fantasyName` == :fantasyName")
    suspend fun getCompanyByName(fantasyName: String): PartnerCompany
}