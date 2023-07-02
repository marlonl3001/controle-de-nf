package br.com.mdr.base.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Company(
    @PrimaryKey
    val key: String = "",
    val email: String,
    val name: String,
    val document: String,
    val companyName: String,
    val phone: String,
    val password: String,
    val keepLogged: Boolean,
    var billingThreshold: Float
)