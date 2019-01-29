package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.google.gson.reflect.TypeToken
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.User
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.GsonUtils
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    var showAdvertisement = "Advertisement"


    fun setResourceId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(setResourceId())

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)


        val local = LocalDataUtils.getString(showAdvertisement)

        val curDay = TimeUtils.curDay()


        var countDownTarget = LocalDataUtils.getString(LocalDataUtils.COUNT_DOWN_TARGET)

        if (!countDownTarget.isEmpty()) {
            var data = countDownTarget.split("_dreamer_")

            if (data[0] == LocalDataUtils.getString(LocalDataUtils.TOKEN)) {

                if (data[2] == "开") {
                    ivAd.visibility = View.GONE
                    rlOtherHint.visibility = View.VISIBLE

                    tvCountDownTarget.text = data[1]

                    window.statusBarColor = resources.getColor(R.color.background)
                    rlLogo.setBackgroundResource(R.color.background)


                    countDownStarter()
                    return
                }


            }
        }

        if (local == curDay) {
            initData()
        } else {
            LocalDataUtils.setString(showAdvertisement, curDay)
            stater()
        }


    }

    private fun countDownStarter() {
        object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                initData()
            }

            override fun onTick(millisUntilFinished: Long) {
                tvCountDownTime.text = (millisUntilFinished / 1000).toString()
            }
        }.start()
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


    fun initData() {
        if (BaseApplication.token.isEmpty()) {

            val token = LocalDataUtils.getString(LocalDataUtils.TOKEN)

            if (token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {

                val strUser = LocalDataUtils.getString(LocalDataUtils.USER)

                val type = object : TypeToken<User>() {}.type

                val user = GsonUtils.getGson().fromJson(strUser, type) as User

                BaseApplication.avatar = LocalDataUtils.getString(LocalDataUtils.AVATAE)

                BaseApplication.userId = user!!.id!!

                BaseApplication.token = token

                BaseApplication.user = user!!

                startActivity(Intent(this, MainActivity::class.java))

                finish()
            }
        } else {
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }

    }
}