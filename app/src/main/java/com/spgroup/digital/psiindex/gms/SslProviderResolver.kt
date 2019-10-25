package com.spgroup.digital.psiindex.gms

import android.app.Activity
import android.content.Intent
import com.google.android.gms.security.ProviderInstaller
import com.spgroup.digital.psiindex.extensions.resolveGooglePlayServices
import com.spgroup.digital.psiindex.extensions.resolveGooglePlayServicesError

class SslProviderResolver {

    private var securityProviderResolved = false

    fun resolve(activity: Activity) {
        activity.resolveGooglePlayServices(activity) { googleApi ->
            if(!securityProviderResolved)
                ProviderInstaller.installIfNeededAsync(activity.applicationContext,
                    object : ProviderInstaller.ProviderInstallListener {
                    override fun onProviderInstalled() {
                        securityProviderResolved = true
                    }

                    override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent) {
                        activity.resolveGooglePlayServicesError(errorCode, googleApi)
                    }
                })
        }
    }
}