package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import com.google.gson.reflect.TypeToken
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.LoginBean
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.GsonUtils
import com.zhangwenshuan.dreamer.util.LocalDataUtils

class SplashActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_splash
    }

    override fun preInitData() {
    }

    override fun initViews() {
    }

    override fun initListener() {
    }

    override fun initData() {
        if (BaseApplication.token.isEmpty()) {
            val loginBean = LocalDataUtils.getString(LocalDataUtils.LOGIN_BEAN)
            if (loginBean.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {


                val type = object : TypeToken<LoginBean>() {}.type

                val login = GsonUtils.getGson().fromJson(loginBean, type) as LoginBean

                BaseApplication.userId = login.user!!.id!!

                BaseApplication.token = login.token!!


                startActivity(Intent(this, MainActivityv1::class.java))

            }
        } else {
            startActivity(Intent(this, MainActivityv1::class.java))
        }

    }
}