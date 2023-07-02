package br.com.mdr.base.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.mdr.base.extension.asBrazilianDate
import br.com.mdr.base.extension.yearFromDate
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Entity
@Parcelize
data class Invoice(
    @PrimaryKey
    val id: Int? = null,
    var company: String,
    var value: Double,
    var number: Int,
    var description: String,
    var month: String,
    var receiveDate: String
): Parcelable {
    val year: Int?
        get() = receiveDate.asBrazilianDate()?.yearFromDate()
}
