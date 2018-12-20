package com.zhangwenshuan.dreamer.util

import okhttp3.Interceptor
import okhttp3.Response

class NetInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var url = request.url()


        val t1 = System.nanoTime()

        logInfo(
            String.format(
                "Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()
            )
        )

        request = request.newBuilder()
            .addHeader("Authorization", "Dreamer " + BaseApplication.token)
            .url(url)
            .build()


        var response = chain.proceed(request)


        if (response.code() == 403) {
            if (!BaseApplication.token.isEmpty()) {
                if (toRefreshToken(chain)) {
                    request = request.newBuilder()
                        .addHeader("Authorization", "Dreamer " + BaseApplication.token)
                        .url(url)
                        .build()

                     response = chain.proceed(request)
                }
            }
        }

        val t2 = System.nanoTime()
        logInfo(
            String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()
            )
        )




        return response
    }

    private fun toRefreshToken(chain: Interceptor.Chain): Boolean {
        val url = NetUtils.getUrl() + "auth/refresh"

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Dreamer " + BaseApplication.token)
            .url(url)
            .build()

        val response = chain.proceed(request)

        if (response.isSuccessful) {
            BaseApplication.token = response.body().toString()
            return true
        }

        return false


    }

}