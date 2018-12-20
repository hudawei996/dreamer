package com.zhangwenshuan.dreamer.util

import com.google.gson.Gson
import com.zhangwenshuan.dreamer.util.LocalDataUtils.Companion.gson


class GsonUtils {

    var gson: Gson? = null


    companion object {


        fun getGson(): Gson {
            if (gson == null) {
                gson = Gson()
            }

            return gson!!
        }
    }
}