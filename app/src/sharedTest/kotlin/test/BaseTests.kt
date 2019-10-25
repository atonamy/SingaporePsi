package test

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import test.service_locator.appTestModule
import java.util.concurrent.TimeUnit

abstract class BaseTests : AutoCloseKoinTest() {

    protected abstract val cacheDir: String

    private lateinit var networkStateTrigger: () -> Unit
    private lateinit var baseUrl: String
    private lateinit var server: MockWebServer


    protected val noResponse = emptyList<MockResponse>()
    protected val successfulResponse =
        listOf(buildResponse("successful_response.json"))
    protected val failedResponse =
        listOf(buildResponse("failed_response.json", 500))

    protected var isOnline = true
        set(status) {
            field = status
            if(::networkStateTrigger.isInitialized)
                networkStateTrigger()
        }


    protected abstract fun getFileFromResources(file: String): String


    protected fun setupServerWithResponse(responses: List<MockResponse>) {
        closeServer()
        server = MockWebServer()
        for(response in responses)
            server.enqueue(response)
        server.start()
        baseUrl = server.url("/").toString()
    }

    protected fun setupWithTestInfrastructure(responses: List<MockResponse>) {
        setupServerWithResponse(responses)
        setupKoin()
    }

    protected fun setupKoin(baseUrl: String = this.baseUrl) {
        stopKoin()
        startKoin {
            modules(
                appTestModule(
                    baseUrl,
                    cacheDir,
                    { isOnline },
                    { networkStateTrigger = it }
                )
            )
        }
    }

    protected fun closeServer() {
        if(::server.isInitialized   )
            server.shutdown()
    }

    private fun buildResponse(fileBody: String, code: Int = 200) =
        MockResponse()
            //.setBodyDelay(500, TimeUnit.MILLISECONDS)
            .setResponseCode(code)
            .setBody(getFileFromResources(fileBody))
}