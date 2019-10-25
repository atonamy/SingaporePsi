package test.service_locator

import com.spgroup.digital.psiindex.network.NetworkInfo
import com.spgroup.digital.psiindex.network.api.PsiDataApi
import com.spgroup.digital.psiindex.network.retrofit.ApiBuilder
import com.spgroup.digital.psiindex.network.retrofit.interceptors.CacheInterceptor
import com.spgroup.digital.psiindex.repositories.PsiDataRepository
import com.spgroup.digital.psiindex.repositories.PsiDataRepositoryImpl
import org.koin.dsl.module
import test.mocks.NetworkInfoMockImpl

fun appTestModule(
    baseApiUrl: String,
    cacheDir: String,
    isOnline: () -> Boolean,
    networkStateListener: (trigger: () -> Unit) -> Unit
    ) = module {

    single<PsiDataRepository> { PsiDataRepositoryImpl() }
    single<PsiDataApi> { ApiBuilder(baseApiUrl).build() }
    single { CacheInterceptor(cacheDir) }
    single<NetworkInfo> { NetworkInfoMockImpl(isOnline, networkStateListener) }
}