package br.com.mdr.controledenf.data.dao

import androidx.room.*
import br.com.mdr.base.domain.Invoice

@Dao
interface InvoiceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: Invoice?)

    @Query("SELECT * FROM Invoice")
    suspend fun getInvoices(): List<Invoice>?

    @Delete
    suspend fun delete(invoice: Invoice)
}