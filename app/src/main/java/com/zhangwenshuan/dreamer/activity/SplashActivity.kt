package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.google.gson.reflect.TypeToken
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.LoginBean
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.GsonUtils
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(setResourceId())

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        initData()

      //  stater()


    }

    private fun stater() {
        object : CountDownTimer(4000, 1000) {
            override fun onFinish() {
                initData()
            }

            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000

                if (time.toInt() == 2) {
                    window.statusBarColor = resources.getColor(R.color.splash_color_2)
                    rlLogo.setBackgroundResource(R.color.splash_color_2)
                    ivAd.setImageResource(R.drawable.pig_year_2)
                } else if (time.toInt() == 1) {
                    window.statusBarColor = resources.getColor(R.color.splash_color_3)
                    rlLogo.setBackgroundResource(R.color.splash_color_3)
                    ivAd.setImageResource(R.drawable.pig_year_3)
                }
            }

        }.start()
    }

    fun setResourceId(): Int {
        return R.layout.activity_splash
    }

    fun initData() {
        if (BaseApplication.token.isEmpty()) {
            val loginBean = LocalDataUtils.getString(LocalDataUtils.LOGIN_BEAN)
            if (loginBean.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
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