package com.zhangwenshuan.dreamer.activity

import android.graphics.Typeface
import android.os.CountDownTimer
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.jetbrains.anko.toast


class RegisterActivity : BaseActivity() {
    override fun setResourceId(): Int = R.layout.activity_register

    override fun preInitData() {

    }

    override fun initViews() {

        val typeface = Typeface.createFromAsset(assets, "title.ttf")

        tvLogo.typeface = typeface

    }

    override fun initListener() {
        ivBack.setOnClickListener { finish() }

        btnVeriCode.setOnClickListener {
            toGetCode()
        }

        btnRegister.setOnClickListener {
            toRegister()
        }

    }

    private fun toRegister() {
        val phone = etPhone.text.toString()

        if (phone.isEmpty()) {
            toast("手机号码不能为空")
            return
        }

        val password = etPassword.text.toString()

        if (password.isEmpty()) {
            toast("密码不能为空")
            return
        }

          if (password.length<6) {
            toast("密码不能小于6位")
            return
        }


        val code = etVeriCode.text.toString()

        if (phone.isEmpty()) {
            toast("验证码不能为空")
            return
        }

        NetUtils.data(NetUtils.getApiInstance().register(phone,password,code), Consumer {
            toast(it.message)

            if (it.code==200){
                finish()
            }
        })
    }

    private fun toGetCode() {

        val phone = etPhone.text.toString()

        if (phone.isEmpty()) {
            toast("手机号码不能为空")
            return
        }

        NetUtils.data(NetUtils.getApiInstance().getCheckCode(phone), Consumer {
            toast(it.message)

            if (it.code == 200) {
                startTimeCountDown(60000)
                btnVeriCode.isClickable = false
            }
        })
    }

    lateinit var timer: CountDownTimer

    private fun startTimeCountDown(time: Long) {

        timer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                btnVeriCode.text = "获取验证码"
                btnVeriCode.isClickable = true
            }

            override fun onTick(millisUntilFinished: Long) {
                btnVeriCode.text = "${(millisUntilFinished / 1000)} s"
            }

        }

        timer.start()
    }

    override fun initData() {
    }


}