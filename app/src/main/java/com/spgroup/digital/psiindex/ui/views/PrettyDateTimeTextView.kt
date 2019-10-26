package com.spgroup.digital.psiindex.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.github.curioustechizen.ago.RelativeTimeTextView
import com.spgroup.digital.psiindex.R

class PrettyDateTimeTextView @JvmOverloads constructor(
context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeTimeTextView(context, attrs, defStyleAttr) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        typeface = (ResourcesCompat.getFont(context, R.font.android))
    }

    override fun getRelativeTimeDisplayString(referenceTime: Long, now: Long): CharSequence {
        val relativeTime = super.getRelativeTimeDisplayString(referenceTime, now)
        return resources.getString(R.string.last_update, relativeTime)
    }
}