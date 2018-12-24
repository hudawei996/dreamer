package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.LoginBean
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.GsonUtils
import com.zhangwenshuan.dreamer.util.LocalDataUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       setContentView(setResourceId())

        object:CountDownTimer(60000,1000){
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()


        initData()
    }

    fun setResourceId(): Int {
        return R.layout.activity_splash
    }

    fun initData() {
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

                finish()
            }
        } else {
            startActivity(Intent(this, MainActivityv1::class.java))

            finish()
        }

    }
}