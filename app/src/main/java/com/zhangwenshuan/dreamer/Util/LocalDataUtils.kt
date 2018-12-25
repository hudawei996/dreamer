package com.zhangwenshuan.dreamer.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhangwenshuan.dreamer.bean.BankCard


class LocalDataUtils {


    companion object {
        val BANK = "bank"

        val LOGIN_BEAN = "login_bean"


        var gson: Gson? = null

        fun getString(key: String): String {
            val utils = BaseApplication.getContext().getSharedPreferences("", Context.MODE_PRIVATE)

            return utils.getString(key, "")
        }

        fun setString(key: String, value: String): Boolean {
            logInfo("set data in local\n$value")

            val utils = BaseApplication.getContext().getSharedPreferences("", Context.MODE_PRIVATE).edit()

            return utils.putString(key, value).commit()
        }


    }


}

fun getBankCarsFromLocal(): MutableList<BankCard>? {
    val strBank = LocalDataUtils.getString(LocalDataUtils.BANK)

    if (strBank != "") {
        val type = object : TypeToken<MutableList<BankCard>>() {}.type

        return GsonUtils.getGson().fromJson(strBank, type)
    }

    return null

}
