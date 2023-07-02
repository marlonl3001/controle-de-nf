package br.com.mdr.base.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class PartnerCompany(
    @PrimaryKey
    val id: Int? = null,
    var document: String,
    var fantasyName: String,
    var companyName: String
): Parcelable
