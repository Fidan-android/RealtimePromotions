package com.example.realtimepromotions.data.api

import androidx.viewbinding.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response
        val token: String = "" //App.appContext.shared(SharedKeys.AccessToken)
        try {
            val newRequest = request
                .newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $token")
//                .addHeader("Mobile-App-Version", BuildConfig.VERSION_CODE.toString())
//                .addHeader("Package-Name", BuildConfig.APPLICATION_ID)
                .build()
            response = chain.proceed(newRequest)
            if (response.code == 401) {
                GlobalScope.launch(Dispatchers.IO) {
//                    AuthRepository().refreshToken()
                }
            }
        } catch (e: Exception) {
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .body(
                    ResponseBody.create(
                        "application/json".toMediaTypeOrNull(),
                        if (e.message.isNullOrEmpty()) "" else e.message!!
                    )
                )
                .message(if (e.message.isNullOrEmpty()) "" else e.message!!)
                .build()
        }

        if (BuildConfig.DEBUG) {
            val responseLogBuilder = StringBuilder()
            responseLogBuilder.append(response.code)
            responseLogBuilder.append(" ")
            responseLogBuilder.append(response.request.method)
            responseLogBuilder.append(" ")
            responseLogBuilder.append(response.request.url)
            responseLogBuilder.append(" ")
            if (response.request.body != null) {
                responseLogBuilder.append("\n--- REQUEST\n")
                responseLogBuilder.append(requestBodyToString(response.request.body))
            }

            val body = response.body!!
            val source = body.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()
            val contentType = body.contentType()
            val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
            if (body.contentLength() != 0L) {
                responseLogBuilder.append("\n--- RESPONSE\n")
                val string = buffer.clone().readString(charset)
                responseLogBuilder.append(string)
            }
        }

        return response
    }

    private fun requestBodyToString(requestBody: RequestBody?): String? {
        requestBody ?: return null
        return try {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            null
        }
    }
}