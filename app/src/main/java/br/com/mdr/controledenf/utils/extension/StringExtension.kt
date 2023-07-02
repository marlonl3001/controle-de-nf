package br.com.mdr.controledenf.utils.extension

import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Html.fromHtml
import android.text.Spanned

fun String.toHtmlSpanned(): Spanned = fromHtml(this, FROM_HTML_MODE_LEGACY)


