package ru.mts.data.main

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okhttp3.mockwebserver.MockResponse
import ru.mts.data.utils.ResourcesReader
import kotlin.random.Random

private val RANDOM_ERROR = Random(System.currentTimeMillis())
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (RANDOM_ERROR.nextBoolean()) {
            return chain.proceed(chain.request())
                .newBuilder()
                .code(400)
                .protocol(Protocol.HTTP_2)
                .build()
        }

        val uri = chain.request().url.toUri().toString()
        val body =
            MockResponse().setBodyFromFile(
                when {
                    uri.endsWith("api/v1/samplelist") -> "assets/news.json"
                    uri.endsWith("api/v1/sample") -> ""
                    else -> ""
                }

            ).getBody()
            ?.asResponseBody("application/json".toMediaTypeOrNull())

        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .body(
                body
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}

fun MockResponse.setBodyFromFile(filName: String): MockResponse {
    val text = ResourcesReader.readText(filName)
    setBody(text)
    return this
}

