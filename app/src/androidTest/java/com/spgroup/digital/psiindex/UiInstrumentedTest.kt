package com.spgroup.digital.psiindex


import android.content.Context
import android.os.Environment
import android.view.View
import androidx.annotation.ColorRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.spgroup.digital.psiindex.ui.activities.MainActivity
import org.awaitility.kotlin.await
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule
import test.BaseTests
import java.util.concurrent.TimeUnit
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.spgroup.digital.psiindex.ui.views.PsiView


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UiInstrumentedTest : BaseTests() {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().context // not targetContext

    @get:Rule
    open val activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(
            MainActivity::class.java,
            true,
            false
        )

    override val cacheDir: String
        get() = Environment.getExternalStorageDirectory().absolutePath

    override fun getFileFromResources(file: String): String
            = context.assets.open(file).bufferedReader().use { it.readText() }

    @After
    fun finisTest() {
        closeServer()
    }

    @Test
    fun testCompleteScreen() {
        setupWithTestInfrastructure(successfulResponse)
        basicActivityTestScope {
            val mapView = activityRule.activity.findViewById<View>(R.id.sgMap)
            mapView != null && mapView.alpha == PsiView.mapAlpha
        }
    }

    @Test
    fun testLoadingScreen() {
        setupWithTestInfrastructure(successfulResponse)
        basicActivityTestScope {
            val loadingView = activityRule.activity.findViewById<View>(R.id.loading)
            loadingView != null && loadingView.alpha == PsiView.loadingAlpha
        }
    }

    @Test
    fun testPsiLegend() {
        setupWithTestInfrastructure(successfulResponse)
        basicActivityTestScope {
            val context = activityRule.activity
            val east = context.findViewById<AppCompatTextView>(R.id.east)
            val west = context.findViewById<AppCompatTextView>(R.id.west)
            val north = context.findViewById<AppCompatTextView>(R.id.north)
            val south = context.findViewById<AppCompatTextView>(R.id.south)
            val central = context.findViewById<AppCompatTextView>(R.id.central)

            east != null &&
                    west != null &&
                    north != null &&
                    south != null &&
                    central != null &&
                    west.intVal <= 50 && west.currentTextColor == context.color(R.color.healthyLevel) &&
                    east.intVal > 50 && east.intVal <= 100 && east.currentTextColor == context.color(R.color.moderateLevel) &&
                    central.intVal <= 50 && central.currentTextColor == context.color(R.color.healthyLevel) &&
                    east.intVal > 50 && south.intVal <= 100 && south.currentTextColor == context.color(R.color.moderateLevel) &&
                    north.intVal > 100 && north.currentTextColor == context.color(R.color.unhealthyLevel)
        }
    }

    private inline fun advancedActivityTestScope(crossinline scope: () -> Unit) {
        activityRule.launchActivity(null)
        scope()
        activityRule.finishActivity()
    }

    private inline fun basicActivityTestScope(crossinline scope: () -> Boolean) {
        advancedActivityTestScope {
            await.atMost(5000L, TimeUnit.MILLISECONDS).until {
                scope()
            }
        }
    }

    private val AppCompatTextView.intVal
        get() = text.toString().toInt()

    private fun Context.color(@ColorRes color: Int) =
        ContextCompat.getColor(this, color)


}
