package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Login
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activit_setting_update_password.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class SettingUpdatePasswordActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activit_setting_update_password
    }

    override fun preInitData() {
    }

    override fun initViews() {

        tvTitle.text="修改密码"

        tvAdd.visibility = View.VISIBLE

        tvAdd.text = resources.getString(R.string.ok)

        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")
    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            val oldPassword = etUpdatePasswordOld.text.toString()

            if (oldPassword.isEmpty() || oldPassword.length < 6) {
                toast("旧密码有误！")
                return@setOnClickListener
            }


            val newPassword = etUpdatePasswordNew.text.toString()

            if (oldPassword.isEmpty() || oldPassword.length < 6) {
                toast("新密码长度需大于6位！")
                return@setOnClickListener
            }

            toUpdatePassword(oldPassword, newPassword)

        }
    }

    private fun toUpdatePassword(oldPassword: String, newPassword: String) {
        NetUtils.data(
            NetUtils.getApiInstance().updatePassword(BaseApplication.userId, oldPassword, newPassword),
            Consumer {
                toast(it.message)
                if (it.code==200){
                    LocalDataUtils.setString(LocalDataUtils.LOGIN_BEAN, "")
                    EventBus.getDefault().post(Login())
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            })
    }

    override fun initData() {

    }
}