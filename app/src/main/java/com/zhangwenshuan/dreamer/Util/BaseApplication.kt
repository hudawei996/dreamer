package com.zhangwenshuan.dreamer.util

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.tencent.bugly.Bugly
import com.zhangwenshuan.dreamer.bean.User


const val TAG = "dreamer"


class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        instance = this

        Bugly.init(getApplicationContext(), "eacb38dfc1", false)

    }


    companion object {
        @JvmStatic
        var userId = 20190101


        @JvmStatic
        var token: String = ""

        @JvmStatic
        var user: User? = null

        @JvmStatic
        var avatar: String = ""


        private lateinit var instance: Application

        fun getContext(): Application {
            return instance
        }

        fun setUserLocal(user: User) {
            this.user = user

            LocalDataUtils.setString(LocalDataUtils.USER, GsonUtils.getGson().toJson(user))
        }

        fun setAvatarLocal(avatar:String) {
            this.avatar = avatar

            LocalDataUtils.setString(LocalDataUtils.AVATAE, avatar)
        }


    }


    fun netListener() {
        val manager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }


    fun registerNetListener() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(android.net.ConnectivityManager.ACTION_RESTRICT_BACKGROUND_CHANGED)
    }

}

fun logInfo(message: String) {
    if (DEBUG) Log.i(TAG, message)
}

fun logError(message: String) {
    if (DEBUG) Log.e(TAG, message)
}


fun getScreenPoint(): Point {
    val point = Point()
    val windowManager = BaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getSize(point)

    return point
}

