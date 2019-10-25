package com.spgroup.digital.psiindex

import android.app.Activity
import android.os.Process
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController
import com.spgroup.digital.psiindex.extensions.hasOpenGl
import com.spgroup.digital.psiindex.extensions.showAlertDialogMessage
import com.spgroup.digital.psiindex.gms.SslProviderResolver
import com.spgroup.digital.psiindex.service_locator.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    private val sslResolver = SslProviderResolver()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        processActivityLifecycle()
        initServiceLocator()
        setupEpoxy()
    }

    private fun setupEpoxy() {
        val handler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
        EpoxyController.defaultDiffingHandler = handler
        EpoxyController.defaultModelBuildingHandler = handler
    }

    private fun initServiceLocator() {
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun processActivityLifecycle()  =
        registerActivityLifecycleCallbacks(ApplicationActivityLifecycle().apply {
            setStartActivityReferenceListener {
                sslResolver.resolve(it)
                resolveOpenGlSupport(it)
            }
        })

    private fun resolveOpenGlSupport(activity: Activity) {
        if(!hasOpenGl)
            activity.showAlertDialogMessage(getString(R.string.no_open_gl_support)) {
                Process.killProcess(Process.myPid())
            }
    }
}