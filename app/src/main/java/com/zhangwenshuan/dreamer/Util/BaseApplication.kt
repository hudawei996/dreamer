package com.zhangwenshuan.dreamer.util

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.widget.Toast


const val userId = 20180101

const val TAG = "dreamer"

class BaseApplication : Application() {

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()

        context = this


    }


    companion object {

        @JvmStatic
        fun getDB() {

        }

    }


    fun netListener() {
        val manager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }


    fun registerNetListener(){
       val intentFilter= IntentFilter()
        intentFilter.addAction(android.net.ConnectivityManager.ACTION_RESTRICT_BACKGROUND_CHANGED)
    }

}


var firstShow: Long = 0

var secondShow: Long = 0

var lastMessage: String = ""

//fun BaseActivity.toast(message: String, time: Int = Toast.LENGTH_SHORT) {
//    if (toast == null) {
//        toast = Toast(this)
//        toast!!.setText(message)
//        toast!!.duration = time
//        toast?.show()
//        firstShow = System.currentTimeMillis()
//    } else {
//        secondShow = System.currentTimeMillis()
//
//        if (lastMessage == message) {
//            if (secondShow - firstShow > Toast.LENGTH_SHORT) {
//                toast?.show()
//            }
//        } else {
//            lastMessage = message
//            toast?.setText(lastMessage)
//            toast?.duration = time
//            toast?.show()
//        }
//    }
//
//    firstShow = secondShow
//
//}

