package com.zhangwenshuan.dreamer.activity


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Event
import com.zhangwenshuan.dreamer.bean.LogoType
import com.zhangwenshuan.dreamer.bean.Password
import com.zhangwenshuan.dreamer.bean.getImageRes
import com.zhangwenshuan.dreamer.database.DatabaseHelper
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.userId
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_password.*
import kotlinx.android.synthetic.main.activity_password_add.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class PasswordAddActivity : BaseActivity() {

    private val LOGO_REQUEST_CODE = 1000

    private lateinit var type: LogoType

    private var isUpload = true

    override fun setResourceId(): Int {
        return R.layout.activity_password_add
    }

    override fun preInitData() {
    }

    override fun initViews() {
        tvPasswordHint.text =
                "1、数据优先保存到本地，再同步到云服务器\n" +
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

                val writer = DatabaseHelper(this).writableDatabase

                val values = ContentValues()
                values.put("name", password.name)
                values.put("username", password.username)
                values.put("password", password.password)
                values.put("user_id", password.userId)
                values.put("type", password.type.ordinal)

                val result = writer.insert(DatabaseHelper.PASSWORD_TABLE, null, values)

                writer.close()

                if (result > 0) {
                    toast("本地保存成功")
                    EventBus.getDefault().post(Event(0, "save-success"))
                    finish()
                } else {
                    toast("本地保存失败")
                }



                Log.e(com.zhangwenshuan.dreamer.util.TAG, "$result")
            }

        }

        ivPasswordLogo.setOnClickListener {
            startActivityForResult(Intent(this@PasswordAddActivity, LogoActivity::class.java), LOGO_REQUEST_CODE)
        }
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

        return Password(passwordName.toString(), username.toString(), password.toString(), userId, type, null)
    }

    override fun initData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOGO_REQUEST_CODE) {
                if (data != null) {
                    val ordinal = data.getIntExtra("data", LogoType.WIFI.ordinal)

                    type = LogoType.values()[ordinal]

                    ivPasswordLogo.setImageResource(getImageRes(type))
                }

            }
        }

    }
}