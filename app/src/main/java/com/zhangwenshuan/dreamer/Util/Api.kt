package com.zhangwenshuan.dreamer.util

import com.zhangwenshuan.dreamer.bean.Result
import com.zhangwenshuan.dreamer.bean.User
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @POST("password/login")
    @FormUrlEncoded
    fun login(@Field("username") username:String,@Field("password") password:String):Observable<Result<User>>


    @POST("password/save")
    @FormUrlEncoded
    fun savePassword(@Field("username") username: String,
                     @Field("name") name:String,
                     @Field("password") password:String,
                     @Field("type") type:Int,
                     @Field("userId") userId:Int):Observable<Result<Any>>

}