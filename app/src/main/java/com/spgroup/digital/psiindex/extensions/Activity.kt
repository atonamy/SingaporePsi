package com.spgroup.digital.psiindex.extensions

import android.app.Activity
import android.os.Process
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.GoogleApiAvailability
import com.spgroup.digital.psiindex.R

fun Activity.resolveGooglePlayServicesError(error: Int,
                                            googleApi: GoogleApiAvailability =
                                                GoogleApiAvailability.getInstance()) {
    if(googleApi.isUserResolvableError(error))
    {
        val errorDialog = googleApi.getErrorDialog(
            this,
            error,
            9000
        )
        errorDialog.setOnDismissListener {
            Process.killProcess(Process.myPid())
        }
        errorDialog.show()
    }
}

inline fun Activity.showAlertDialogMessage(message: String, crossinline dismiss: () -> Unit) {
    val dialog = AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(R.string.exit_button) {_, _ ->
            dismiss()
        }
        .create()
    dialog.setOnDismissListener {
        dismiss()
    }
    dialog.show()
}
