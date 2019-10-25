package com.spgroup.digital.psiindex.service_locator

import com.spgroup.digital.psiindex.Settings
import com.spgroup.digital.psiindex.network.NetworkInfo
import com.spgroup.digital.psiindex.network.NetworkInfoImpl
import com.spgroup.digital.psiindex.network.api.PsiDataApi
import com.spgroup.digital.psiindex.network.retrofit.ApiBuilder
import com.spgroup.digital.psiindex.network.retrofit.interceptors.CacheInterceptor
import com.spgroup.digital.psiindex.repositories.PsiDataRepository
import com.spgroup.digital.psiindex.repositories.PsiDataRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {
    single<PsiDataRepository> { PsiDataRepositoryImpl() }
    single<PsiDataApi> { ApiBuilder(Settings.baseApiUrl).build() }
    single { CacheInterceptor(androidContext().cacheDir.absolutePath) }
    single<NetworkInfo> { NetworkInfoImpl(androidContext()) }
}