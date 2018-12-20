package com.zhangwenshuan.dreamer.activity


import android.content.Intent
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.LogoType
import com.zhangwenshuan.dreamer.bean.Password
import com.zhangwenshuan.dreamer.bean.PasswordAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_password.*
import kotlinx.android.synthetic.main.activity_password_add.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class PasswordAddActivity : BaseActivity() {

    private val LOGO_REQUEST_CODE = 1000


    private var isUpload = true

    override fun setResourceId(): Int {
        return R.layout.activity_password_add
    }

    override fun preInitData() {
    }

    override fun initViews() {
        tvPasswordHint.text =
                "1、实时保存云服务器\n" +
                "2、数据会采用加密方式保存，管理人员是无法查阅您的密码的\n" +
                "3、为了确保您的资金安全，不要保存银行卡等等密码"
    }

    override fun initListener() {
        ivBack.setOnClickListener { finish() }


        cbUpload.setOnCheckedChangeListener { buttonView, isChecked ->
            isUpload = isChecked
        }


        btnSavePassword.setOnClickListener {

            val password = checkData()

            if (password != null) {

                toSavePassword(password)

            }

        }

        ivPasswordLogo.setOnClickListener {
            startActivityForResult(Intent(this@PasswordAddActivity, LogoActivity::class.java), LOGO_REQUEST_CODE)
        }
    }

    private fun toSavePassword(password: Password) {
        NetUtils.data(NetUtils.getApiInstance().savePassword(
            password.username, password.name,
            password.password, BaseApplication.userId
        ), Consumer {
            if (it.code == 200) {
                EventBus.getDefault().post(PasswordAdd())
                finish()
            }
            toast(it.message)
        })
    }


    private fun checkData(): Password? {
        val passwordName = etPasswordName.text

        if (passwordName == null || passwordName.trim().isEmpty()) {
            toast("平台名称不能为空")
            return null
        }

        val username = etUsername.text

        if (username == null || username.trim().isEmpty()) {
            toast("用户不能为空")
            return null
        }

        val password = etPassword.text

        if (password == null || password.trim().isEmpty()) {
            toast("密码不能为空")
            return null
        }

        return Password(passwordName.toString(), username.toString(), password.toString(), BaseApplication.userId, null)
    }

    override fun initData() {
    }


}