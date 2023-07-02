package br.com.mdr.base.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ExpenseCategory(
    @PrimaryKey
    val id: Int? = null,
    var name: String,
    var description: String
): Parcelable
