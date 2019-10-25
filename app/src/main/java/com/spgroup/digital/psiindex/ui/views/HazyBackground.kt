package com.spgroup.digital.psiindex.ui.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.spgroup.digital.psiindex.R
import com.spgroup.digital.psiindex.Settings
import com.spgroup.digital.psiindex.extensions.getRawFileAsString
import com.spgroup.digital.psiindex.extensions.hasOpenGl
import com.spgroup.digital.psiindex.ui.views.renderers.HazyBackgroundRenderer

class HazyBackground : GLSurfaceView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        if(context.hasOpenGl) {
            preserveEGLContextOnPause = true
            setEGLContextClientVersion(Settings.openGlVersionSupport.first)
            setRenderer(
                HazyBackgroundRenderer(
                    resources.getRawFileAsString(R.raw.background_vert),
                    resources.getRawFileAsString(R.raw.background_frag)
                )
            )
        }
    }
}