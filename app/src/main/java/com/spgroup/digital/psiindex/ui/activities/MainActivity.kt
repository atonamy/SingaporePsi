package com.spgroup.digital.psiindex.ui.activities

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.BaseMvRxActivity
import android.os.Build
import com.spgroup.digital.psiindex.R


class MainActivity : BaseMvRxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        hideNavigationBar()
    }

    private fun hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}
