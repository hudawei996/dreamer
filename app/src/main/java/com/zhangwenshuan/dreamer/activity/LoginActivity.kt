package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import com.zhangwenshuan.dreamer.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:BaseActivity() {
    override fun initData() {
    }

    override fun initListener() {
        tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))

        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }
    }

    override fun initViews() {

    }

    override fun preInitData() {
    }

    override fun setResourceId(): Int =R.layout.activity_login

}