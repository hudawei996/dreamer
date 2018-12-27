package com.zhangwenshuan.dreamer.util

import android.util.Log
import com.zhangwenshuan.dreamer.bean.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


const val PROD_URL = "https://www.zhangwenshuan.com/dreamer/"

const val DEV_URL = "http://192.168.1.107:8080/"

const val DEBUG = true

class NetUtils {


    companion object {

      private  var retrofit: Retrofit? = null

     private   var api: Api? = null

      private  var client:OkHttpClient?=null

        fun getUrl():String{
           if (DEBUG) return DEV_URL else return PROD_URL
        }

        fun getApiInstance(): Api {

            var url = ""

            if (DEBUG) url = DEV_URL else url = PROD_URL

            if (retrofit == null) {

                client=OkHttpClient.Builder()
                    .addNetworkInterceptor(NetInterceptor())
                    .connectTimeout(10000,TimeUnit.SECONDS)
                    .build()

                retrofit = Retrofit.Builder().baseUrl(url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                        GsonConverterFactory.create())
                    .client(client)
                    .build()
                api = retrofit!!.create(Api::class.java)

            }

            return api!!
        }

        fun <T> data(observable: Observable<Result<T>>, result:Consumer<Result<T>>){

            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result, Consumer<Throwable> {
                    it.printStackTrace()
                    Log.e("net","net error：${it.message}")
                })
        }

    }





}