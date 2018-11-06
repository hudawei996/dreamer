package com.zhangwenshuan.dreamer.Util

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.zhangwenshuan.dreamer.activity.BaseActivity
import org.jetbrains.anko.toast


class BaseApplication : Application() {

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()

        context = this
    }


    fun getApplcation()=context

}

var toast: Toast? = null

var firstShow: Long = 0

var secondShow: Long = 0

var lastMessage: String = ""

fun BaseActivity.toast(message: String, time: Int = Toast.LENGTH_SHORT) {
    if (toast == null) {
        toast = Toast(this)
        toast!!.setText(message)
        toast!!.duration = time
        toast?.show()
        firstShow = System.currentTimeMillis()
    } else {
        secondShow = System.currentTimeMillis()

        if (lastMessage == message) {
            if (secondShow - firstShow > Toast.LENGTH_SHORT) {
                toast?.show()
            }
        } else {
            lastMessage = message
            toast?.setText(lastMessage)
            toast?.duration = time
            toast?.show()
        }
    }

    firstShow = secondShow


}