package com.spgroup.digital.psiindex.repositories

import android.accounts.NetworkErrorException
import android.provider.ContactsContract
import com.spgroup.digital.psiindex.data.model.PsiDataResponse
import com.spgroup.digital.psiindex.network.NetworkInfo
import com.spgroup.digital.psiindex.network.api.PsiDataApi
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDateTime
import retrofit2.HttpException
import java.lang.Exception

class PsiDataRepositoryImpl : PsiDataRepository, KoinComponent {

    private val api by inject<PsiDataApi>()
    private val networkInfo by inject<NetworkInfo>()

    override fun getPsiData(): Single<PsiDataResponse.Singapore> =
        api.retrievePsiData().onErrorResumeNext {
            if(!networkInfo.isNetworkAvailable)
                Single.error(NetworkErrorException(networkInfo.whenOfflineMessage))
            else
                Single.error(it)
        }.map {
            val response = it.body()
            if(it.isSuccessful && response?.singapore != null &&
                response.singapore.isNotEmpty())
                response.singapore[0]
            else
                throw HttpException(it)
        }

}