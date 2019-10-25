package com.spgroup.digital.psiindex.view_model


import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.spgroup.digital.psiindex.data.state.PsiDataState
import com.spgroup.digital.psiindex.network.NetworkInfo
import com.spgroup.digital.psiindex.repositories.PsiDataRepository
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit




class PsiDataViewModel (
    initialState: PsiDataState,
    private val psiDataRepository: PsiDataRepository
) : MvRxViewModel<PsiDataState>(initialState), KoinComponent {

    private val networkInfo by inject<NetworkInfo>()

    init {
        networkInfo.registerNetworkStateListener {
            setState {
                copy(isNetworkAvailable = networkInfo.isNetworkAvailable)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkInfo.unregisterNetworkStateListener()
    }

    fun reloadPsiData() {
        psiDataRepository.getPsiData().delay(1, TimeUnit.SECONDS).execute {
            copy(psiRequest = it, previousRequest = psiRequest,
                isNetworkAvailable = networkInfo.isNetworkAvailable)
        }
    }

    fun requestPsiData() = withState {
        if(it.psiRequest is Uninitialized)
            reloadPsiData()
        else
            setState {
                copy(previousRequest = psiRequest,
                    isNetworkAvailable = networkInfo.isNetworkAvailable
                    )
            }
    }



    companion object : MvRxViewModelFactory<PsiDataViewModel, PsiDataState> {
        override fun create(viewModelContext: ViewModelContext, state: PsiDataState): PsiDataViewModel {
            val activity = viewModelContext.activity
            val repository by activity.inject<PsiDataRepository>()
            return PsiDataViewModel(state, repository)
        }
    }

}