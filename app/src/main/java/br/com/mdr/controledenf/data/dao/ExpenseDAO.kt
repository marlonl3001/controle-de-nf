package br.com.mdr.controledenf.data.dao

import androidx.room.*
import br.com.mdr.base.domain.Expense

@Dao
interface ExpenseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense?)

    @Query("SELECT * FROM Expense")
    suspend fun getExpenses(): List<Expense>?

    @Delete
    suspend fun delete(expense: Expense)
}