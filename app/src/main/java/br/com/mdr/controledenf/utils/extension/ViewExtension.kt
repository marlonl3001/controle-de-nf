package br.com.mdr.controledenf.utils.extension

import android.view.View
import androidx.core.content.ContextCompat
import br.com.mdr.controledenf.R
import com.google.android.material.snackbar.Snackbar

fun View.errorSnack(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(ContextCompat.getColor(this.context, R.color.colorError))
    snackBar.show()
}

fun View.successSnack(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackBar.setBackgroundTint(ContextCompat.getColor(this.context, R.color.colorSuccess))
    snackBar.show()
}