package com.spgroup.digital.psiindex.extensions

import android.content.res.Resources
import androidx.annotation.RawRes

fun Resources.getRawFileAsString(@RawRes id: Int) =
    openRawResource(id).bufferedReader().use { it.readText() }