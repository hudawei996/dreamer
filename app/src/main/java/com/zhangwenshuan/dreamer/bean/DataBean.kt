package com.zhangwenshuan.dreamer.bean

import com.zhangwenshuan.dreamer.R


/**
 * define bean
 */

data class Password(  var name:String,  var username:String,  var password:String,

                      var userId:Int?, var type: LogoType,

                    var id:Int?)

data class Event(var code:Int,var emssage:String)

data class Logo(var type: LogoType,var name:String)

data class User(var username:String,var id:Int?,var password: String)

data class Result<T>(var code: Int,var msg:String,var data: T)

/**
 * define enum
 */
enum class LogoType{
    AI_QI_YI,TEN_XUN,YOU_KU,WEI_BO,WIFI
}




fun  getImageRes(type: LogoType):Int{
    when(type){
        LogoType.AI_QI_YI->return R.mipmap.ic_ai_qi_yi
        LogoType.TEN_XUN->return R.mipmap.ic_tengxun
        LogoType.YOU_KU->return R.mipmap.ic_youku
        LogoType.WEI_BO->return R.mipmap.ic_weibo
        LogoType.WIFI->return R.mipmap.ic_wifi
        else ->return R.mipmap.ic_launcher
    }


}

