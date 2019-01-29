package com.zhangwenshuan.dreamer.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhangwenshuan.dreamer.bean.Bank
import com.zhangwenshuan.dreamer.bean.BankCard


class LocalDataUtils {


    companion object {
        val BANK = "bank"

        val BANK_CARD = "bank_card"

        val BUDGET_ITEM="budget"


        val LOGIN_BEAN = "login_bean"


        val USER="user"

        val TOKEN="token"


        var gson: Gson? = null


        val LOCAL_PASSWORD="local_password"

        val LOCAL_PASSWORD_USER="local_user"

        val LOCAL_PASSWORD_STATE="local_state"

        val AVATAE: String="avatar_url"

        val COUNT_DOWN_TARGET="target"

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
