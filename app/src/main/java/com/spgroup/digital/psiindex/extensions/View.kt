package com.spgroup.digital.psiindex.extensions

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.spgroup.digital.psiindex.R

inline fun View.showRetryActionSnackbar(message: String, crossinline action: () -> Unit) {
    val sb = Snackbar.make(this, message,
        Snackbar.LENGTH_INDEFINITE)
    sb.setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
    sb.setAction(context.getString(R.string.retry_button)) {
        sb.dismiss()
        action()
    }
    sb.show()
}