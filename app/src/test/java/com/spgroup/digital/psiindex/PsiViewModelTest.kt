package com.spgroup.digital.psiindex

import android.accounts.NetworkErrorException
import com.airbnb.mvrx.*
import com.spgroup.digital.psiindex.data.state.PsiDataState
import com.spgroup.digital.psiindex.repositories.PsiDataRepository
import com.spgroup.digital.psiindex.ui.views.PsiView
import com.spgroup.digital.psiindex.view_model.PsiDataViewModel
import org.awaitility.kotlin.await
import org.junit.After
import org.junit.Test

import org.koin.test.inject
import test.BaseTests
import java.io.File
import java.util.concurrent.TimeUnit
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PsiViewModelTest : BaseTests() {

    val viewModel: PsiDataViewModel
        by lazy {
            val psiDataRepo by inject<PsiDataRepository>()
            PsiDataViewModel(PsiDataState(), psiDataRepo)
        }


    @After
    fun finishTest() {
        closeServer()
    }


    @Test
    fun initialState_isCorrect() {
        setupWithTestInfrastructure(noResponse)
        withState(viewModel) {
            assertTrue(it.psiRequest is Uninitialized)
            assertTrue(it.previousRequest is Uninitialized)
            assertTrue(it.isNetworkAvailable)
            assertEquals(it.viewSate, PsiView.ViewSate.Idle)
        }
    }

    @Test
    fun loadingState_isCorrect() {
        setupWithTestInfrastructure(successfulResponse)
        viewModel.requestPsiData()
        verifyStateWithPending {
            it.psiRequest is Loading &&
                    it.previousRequest is Uninitialized &&
                    it.isNetworkAvailable &&
                    it.viewSate == PsiView.ViewSate.Idle
        }
    }

    @Test
    fun successState_isCorrect() {
        setupWithTestInfrastructure(successfulResponse)
        viewModel.requestPsiData()
        verifyStateWithPending {
            it.psiRequest is Success &&
                    it.previousRequest is Loading &&
                    it.isNetworkAvailable &&
                    it.viewSate == PsiView.ViewSate.PopupContent
        }
    }

    @Test
    fun failState_isCorrect() {
        setupWithTestInfrastructure(failedResponse)
        viewModel.requestPsiData()
        verifyStateWithPending {
            it.psiRequest is Fail &&
                    it.previousRequest is Loading &&
                    it.isNetworkAvailable &&
                    it.viewSate == PsiView.ViewSate.Idle
        }
    }

    @Test
    fun reloadState_isCorrect() {
        setupWithTestInfrastructure(successfulResponse)
        viewModel.requestPsiData()
        Thread.sleep(2000)
        viewModel.reloadPsiData()
        verifyStateWithPending {
            it.psiRequest is Loading &&
                    it.previousRequest is Success &&
                    it.isNetworkAvailable &&
                    it.viewSate == PsiView.ViewSate.Reload
        }
    }

    @Test
    fun loadedState_isCorrect() {
        setupWithTestInfrastructure(successfulResponse)
        viewModel.requestPsiData()
        Thread.sleep(2000)
        viewModel.requestPsiData()
        verifyStateWithPending {
            it.psiRequest is Success &&
                    it.previousRequest is Success &&
                    it.isNetworkAvailable &&
                    it.viewSate == PsiView.ViewSate.ShowContent
        }
    }

    @Test
    fun offlineState_isCorrect() {
        setupKoin("http://0")
        viewModel.requestPsiData()
        isOnline = false
        verifyStateWithPending {
            val request = it.psiRequest
            request is Fail &&
                    it.previousRequest is Loading &&
                    !it.isNetworkAvailable &&
                    it.viewSate == PsiView.ViewSate.Idle &&
                    request.error is NetworkErrorException
        }
    }

    @Test
    fun interruptState_isCorrect() {
        setupWithTestInfrastructure(successfulResponse)
        viewModel.requestPsiData()
        Thread.sleep(2000)
        isOnline = false
        verifyStateWithPending {
            print(it.viewSate)
                it.psiRequest is Success &&
                    it.previousRequest is Loading &&
                    !it.isNetworkAvailable &&
                    it.viewSate == PsiView.ViewSate.PopupContent
        }
    }


    private inline fun verifyStateWithPending(awaitMili: Long = 5000L,
                                              crossinline condition: (PsiDataState) -> Boolean) {

        await.atMost(awaitMili, TimeUnit.MILLISECONDS).until {
            withState(viewModel) { condition(it) }
        }
    }

    override val cacheDir: String
        get() = System.getProperty("java.io.tmpdir")

    override fun getFileFromResources(file: String): String {
        val classLoader = javaClass.classLoader
        val resource = classLoader!!.getResource(file)
        return String(File(resource.path).readBytes())
    }
}
