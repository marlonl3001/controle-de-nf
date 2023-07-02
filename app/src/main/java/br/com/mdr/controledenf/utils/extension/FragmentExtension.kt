package br.com.mdr.controledenf.utils.extension

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

const val BOTTOM_SHEET_FLAG = "BOTTOM_SHEET_FLAG"

fun Fragment.navigateTo(direction: NavDirections) {
    try {
        findNavController().navigate(direction)
    } catch (e: IllegalArgumentException) {
        Timber.e(e)
    }
}

fun Fragment.pop() {
    try {
        findNavController().navigateUp()
    } catch (e: IllegalArgumentException) {
        Timber.e(e)
    }
}

fun Fragment.showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
    activity?.supportFragmentManager?.let {
        bottomSheet.show(it, BOTTOM_SHEET_FLAG)
    }
}