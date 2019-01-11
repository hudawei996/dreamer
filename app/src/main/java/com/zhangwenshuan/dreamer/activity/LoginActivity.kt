package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.text.Editable
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.GsonUtils
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity() {
    override fun initData() {
        etPhone.text=Editable.Factory.getInstance().newEditable(LocalDataUtils.getString(LocalDataUtils.LOCAL_PASSWORD_USER))
    }

    override fun initListener() {
        tvRegister.setOnClickListener {

            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }

        btnLogin.setOnClickListener {
            toLogin()
        }
    }

    private fun toLogin() {
        val username = etPhone.text.toString()

        val password = etPassword.text.toString()

        if (username.isEmpty()) {
            toast("手机号码不能为空")
            return
        }

        if (password.isEmpty()) {
            toast("密码不能为空")
            return
        }

        NetUtils.data(NetUtils.getApiInstance().login(username, password), Consumer {
            toast(it.message)

            if (it.code == 200) {
                BaseApplication.token = it.data.token!!

                BaseApplication.userId = it.data.user!!.id!!

                BaseApplication.user=it.data.user


                LocalDataUtils.setString(LocalDataUtils.TOKEN, GsonUtils.getGson().toJson(it.data.token))

                LocalDataUtils.setString(LocalDataUtils.USER, GsonUtils.getGson().toJson(it.data.user))

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                finish()


                LocalDataUtils.setString(LocalDataUtils.LOCAL_PASSWORD_USER,username)
            }

        })
    }

    override fun initViews() {

    }

    override fun preInitData() {
    }

    override fun setResourceId(): Int = R.layout.activity_login


}