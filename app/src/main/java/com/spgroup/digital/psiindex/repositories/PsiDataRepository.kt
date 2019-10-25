package com.spgroup.digital.psiindex.repositories

import com.spgroup.digital.psiindex.data.model.PsiDataResponse
import io.reactivex.Single

interface PsiDataRepository {
    fun getPsiData(): Single<PsiDataResponse.Singapore>
}