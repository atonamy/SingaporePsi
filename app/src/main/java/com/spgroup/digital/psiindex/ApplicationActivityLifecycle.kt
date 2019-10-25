package com.spgroup.digital.psiindex

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ApplicationActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private lateinit var onActivityRef: (Activity) -> Unit

    fun setStartActivityReferenceListener(onActivityRef: (Activity) -> Unit) {
        this.onActivityRef = onActivityRef
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {
        if(::onActivityRef.isInitialized && activity != null)
            onActivityRef(activity)
    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }
}