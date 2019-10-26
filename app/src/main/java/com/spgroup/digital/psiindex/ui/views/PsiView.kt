package com.spgroup.digital.psiindex.ui.views

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.spgroup.digital.psiindex.R
import com.spgroup.digital.psiindex.data.model.PsiDataResponse
import kotlinx.android.synthetic.main.map_sg.view.*
import kotlinx.android.synthetic.main.psi_content.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId


@ModelView(defaultLayout = R.layout.psi_content)
class PsiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

    companion object {
        private const val duration = 500L
        const val loadingAlpha = .4f
        const val mapAlpha = .6f
        private const val alphaOffset = .6f
    }

    enum class ViewSate {
        Idle,
        PopupContent,
        ShowContent,
        Reload
    }


    @ModelProp
    fun setContentState(state: ViewSate) =
        when (state) {
            ViewSate.Idle -> idle()
            ViewSate.PopupContent -> popupContent()
            ViewSate.ShowContent -> showContent()
            ViewSate.Reload -> reload()
        }


    @ModelProp
    fun setModel(@Nullable model: PsiDataResponse.Regions?) {
        if(model == null)
            return

        north.setTextColor(determinateColorSensitivity(model.north))
        north.text = model.north.toString()

        south.setTextColor(determinateColorSensitivity(model.south))
        south.text = model.south.toString()

        west.setTextColor(determinateColorSensitivity(model.west))
        west.text = model.west.toString()

        east.setTextColor(determinateColorSensitivity(model.east))
        east.text = model.east.toString()

        central.setTextColor(determinateColorSensitivity(model.central))
        central.text = model.central.toString()
    }

    @ModelProp
    fun setLastUpdate(@Nullable dateTime: LocalDateTime?)
    {
        if(dateTime != null)
            lastUpdate.setReferenceTime(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
    }


    private fun setPsiVisible(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        north.visibility = visibility
        south.visibility = visibility
        west.visibility = visibility
        east.visibility = visibility
        central.visibility = visibility
    }

    private fun idle() {
        sgMap.alpha = 0f
        loading.alpha = loadingAlpha
        loading.visibility = View.VISIBLE
        fadeIn(loading) {
            loading.playAnimation()
        }
        setPsiVisible(false)
    }

    private fun popupContent() {
        setDefaultAlpha()
        fadeOut(loading) {
            loading.visibility = View.GONE
            lastUpdate.visibility = View.VISIBLE
        }
        zoomIn(sgMap) {
            flipAll()
        }
    }

    private fun showContent() {
        sgMap.alpha = mapAlpha
        loading.visibility = View.GONE
        lastUpdate.visibility = View.VISIBLE
    }

    private fun reload() {
        setDefaultAlpha()
        setPsiVisible(true)
        loading.visibility = View.VISIBLE
        zoomOut(map) {
            lastUpdate.visibility = View.GONE
            sgMap.alpha = 0f
            setPsiVisible(false)
        }
        fadeIn(loading) {
            loading.playAnimation()
        }
    }

    private fun setDefaultAlpha() {
        loading.alpha = loadingAlpha
        sgMap.alpha = mapAlpha
    }

    private fun flipAll() {
        flip(east)
        flip(west)
        flip(north)
        flip(south)
        flip(central)
    }

    private fun determinateColorSensitivity(level: Int): Int {
        return when {
            level <= 50 -> ContextCompat.getColor(context, R.color.healthyLevel)
            level <= 100 -> ContextCompat.getColor(context, R.color.moderateLevel)
            else -> ContextCompat.getColor(context, R.color.unhealthyLevel)
        }
    }

    private fun flip(view: View) {
        view.visibility = View.VISIBLE
        val anim = ObjectAnimator.ofFloat(view, "rotationY", 90f, 0f)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration
        anim.start()
    }

    private inline fun fadeOut(view: View, crossinline done: () -> Unit = {}) {
        val anim = AlphaAnimation(loading.alpha+alphaOffset, .0f)
        anim.duration = duration
        anim.amimListener(done)
        view.startAnimation(anim)
    }

    private inline fun fadeIn(view: View, crossinline done: () -> Unit = {}) {
        val anim = AlphaAnimation(0f, loading.alpha+alphaOffset)
        anim.duration = duration
        anim.amimListener(done)
        view.startAnimation(anim)
    }

    private inline fun zoomIn(view: View, crossinline done: () -> Unit = {}) {
        val anim = ScaleAnimation(0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = duration
        anim.amimListener (done)
        view.startAnimation(anim)
    }

    private inline fun zoomOut(view: View, crossinline done: () -> Unit = {}): Animation {
        val anim = ScaleAnimation(1f, 0f, 1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = duration
        anim.amimListener (done)
        view.startAnimation(anim)
        return anim
    }

    private inline fun Animation.amimListener(crossinline done: () -> Unit = {}) {
        setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                done()
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }
}