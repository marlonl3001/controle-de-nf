package br.com.mdr.controledenf.utils.extension

import java.text.NumberFormat
import java.util.Currency

private const val CURRENCY_FORMAT = "BRL"

fun Double.toCurrencyString(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(CURRENCY_FORMAT)
    return format.format(this)
}