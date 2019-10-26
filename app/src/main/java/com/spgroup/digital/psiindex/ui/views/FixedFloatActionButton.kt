package com.spgroup.digital.psiindex.ui.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FixedFloatActionButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    override fun show() {
        super.show()
        isEnabled = true
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        super.setOnClickListener {
            isEnabled = false
            listener?.onClick(it)
        }
    }

}