package com.spgroup.digital.psiindex.extensions

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.spgroup.digital.psiindex.Settings
import android.R.attr.orientation
import android.util.TypedValue
import com.spgroup.digital.psiindex.R


inline fun Context.resolveGooglePlayServices(activity: Activity? = null,
                                             resolver: (GoogleApiAvailability) -> Unit = {}) {
    val googleApi = GoogleApiAvailability.getInstance()
    val result = googleApi.isGooglePlayServicesAvailable(this)
    if (result != ConnectionResult.SUCCESS) {
        activity?.resolveGooglePlayServicesError(result, googleApi)
        return
    }
    resolver(googleApi)
}

val Context.hasOpenGl: Boolean
    get() {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = am.deviceConfigurationInfo
        return info.reqGlEsVersion >= Settings.openGlVersionSupport.second
    }

