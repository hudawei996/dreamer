package com.zhangwenshuan.dreamer.util

import com.zhangwenshuan.dreamer.bean.Login
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus

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

        val token="Dreamer " + BaseApplication.token

        request = request.newBuilder()
            .addHeader("Authorization", token)
            .url(url)
            .build()


        var response = chain.proceed(request)





        if (response.code() == 403) {
            logError("---------403--------")
            EventBus.getDefault().post(Login(1))
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


}