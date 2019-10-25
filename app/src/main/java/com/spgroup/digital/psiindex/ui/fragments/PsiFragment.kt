package com.spgroup.digital.psiindex.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*

import kotlinx.android.synthetic.main.fragment_psi.*
import com.spgroup.digital.psiindex.R
import com.spgroup.digital.psiindex.data.model.PsiDataResponse
import com.spgroup.digital.psiindex.data.state.PsiDataState
import com.spgroup.digital.psiindex.extensions.showRetryActionSnackbar
import com.spgroup.digital.psiindex.ui.views.PsiView
import com.spgroup.digital.psiindex.ui.views.psiView
import com.spgroup.digital.psiindex.view_model.PsiDataViewModel


/**
 * A simple [Fragment] subclass.
 */
class PsiFragment : BaseMvRxFragment() {

    private val psiViewModel: PsiDataViewModel by fragmentViewModel()

    override fun invalidate() = withState(psiViewModel) {
        handleState(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_psi, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setReload()
        psiViewModel.requestPsiData()
    }

    override fun onPause() {
        super.onPause()
       background.onPause()
    }

    override fun onResume() {
        super.onResume()
        background.onResume()
    }


    private fun renderBody(sg: PsiDataResponse.Singapore?, viewState: PsiView.ViewSate) {
        body.withModels {
            psiView {
                id(0)
                model(sg?.data?.psi)
                lastUpdate(sg?.lastUpdate)
                contentState(viewState)
            }
        }
    }


    private fun handleState(state: PsiDataState) {
        when(state.psiRequest) {
            is Loading -> {
                reload.hide()
                renderBody(state.previousRequest(), state.viewSate)
            }
            is Success -> {
                if(state.isNetworkAvailable) reload.show() else reload.hide()
                renderBody(state.psiRequest(), state.viewSate)
            }
            is Fail -> {
                reload.hide()
                body.showRetryActionSnackbar(state.psiRequest.error.message ?:
                    getString(R.string.unknown_error)) {
                        psiViewModel.reloadPsiData()
                }
                renderBody(state.previousRequest(), state.viewSate)
            }
            else -> reload.hide()
        }
    }

    private fun setReload() {
        reload.setOnClickListener {
            psiViewModel.reloadPsiData()
        }
    }
}
