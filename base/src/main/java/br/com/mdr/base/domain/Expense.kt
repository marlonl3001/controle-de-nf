package br.com.mdr.base.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.mdr.base.extension.asBrazilianDate
import br.com.mdr.base.extension.monthFromDate
import br.com.mdr.base.extension.yearFromDate
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Expense(
    @PrimaryKey
    val id: Int? = null,
    var category: String,
    var value: Double,
    var name: String,
    var paymentDate: String,
    var competencyDate: String,
    var company: String
): Parcelable {
    val year: Int?
        get() = paymentDate.asBrazilianDate()?.yearFromDate()

    val month: Int
        get() = paymentDate.asBrazilianDate()?.monthFromDate() ?: 0
}
