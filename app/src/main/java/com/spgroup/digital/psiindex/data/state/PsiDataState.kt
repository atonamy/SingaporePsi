package com.spgroup.digital.psiindex.data.state

import com.airbnb.mvrx.*
import com.spgroup.digital.psiindex.data.model.PsiDataResponse
import com.spgroup.digital.psiindex.ui.views.PsiView

data class PsiDataState (
    val psiRequest: Async<PsiDataResponse.Singapore> = Uninitialized,
    val previousRequest: Async<PsiDataResponse.Singapore> = psiRequest,
    val isNetworkAvailable: Boolean = true
) : MvRxState {

    val viewSate: PsiView.ViewSate
        get() {
            return when {
                previousRequest is Success && psiRequest is Loading -> PsiView.ViewSate.Reload
                previousRequest is Loading && psiRequest is Success -> PsiView.ViewSate.PopupContent
                psiRequest is Success -> PsiView.ViewSate.ShowContent
                else -> PsiView.ViewSate.Idle
            }
        }
}