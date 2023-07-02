package br.com.mdr.controledenf.data.dao

import androidx.room.*
import br.com.mdr.base.domain.ExpenseCategory

@Dao
interface ExpenseCategoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: ExpenseCategory?)

    @Query("SELECT * FROM ExpenseCategory ORDER BY id")
    suspend fun getCategories(): List<ExpenseCategory>?

    @Query("SELECT * FROM ExpenseCategory WHERE `name` == :name")
    suspend fun getCategoryByName(name: String): ExpenseCategory
}