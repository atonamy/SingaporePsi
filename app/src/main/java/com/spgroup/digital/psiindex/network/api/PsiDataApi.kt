package com.spgroup.digital.psiindex.network.api

import com.spgroup.digital.psiindex.data.model.PsiDataResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface PsiDataApi {
    @Headers(
        "accept: application/json"
    )
    @GET("environment/psi")
    fun retrievePsiData(): Single<Response<PsiDataResponse>>
}