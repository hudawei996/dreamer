package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.util.Log
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Result
import com.zhangwenshuan.dreamer.bean.User
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override fun initData() {
    }

    override fun initListener() {
        tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))

        }
    }

    override fun initViews() {

    }

    override fun preInitData() {
    }

    override fun setResourceId(): Int = R.layout.activity_login



}