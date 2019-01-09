package com.zhangwenshuan.dreamer.activity

import android.text.Editable
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import kotlinx.android.synthetic.main.activity_setting_password.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.jetbrains.anko.toast

class SettingPasswordActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_setting_password
    }

    lateinit var password: String

    var state = false

    override fun preInitData() {
        val text = LocalDataUtils.getString(LocalDataUtils.LOCAL_PASSWORD_STATE)

        if (!text.isEmpty()) {

            val strs = text.split(" ")


            if (strs[0].toInt() == BaseApplication.userId) {
                if (strs[1] == "open") {
                    tvPasswordState.text = "当前状态：已开启"

                    tvSave.text = "关闭"

                    state = true
                } else {
                    tvPasswordState.text = "当前状态：已关闭"

                    tvSave.text = "开启"

                    state = false
                }
                etPasswordLocal.text = Editable.Factory.getInstance().newEditable(strs[2])


            }


        }

    }

    override fun initViews() {
        tvTitle.text = "开屏密码"
    }

    override fun initListener() {
        tvSave.setOnClickListener {
            val text = etPasswordLocal.text.toString()

            if (text.isEmpty() || text.length != 6) {
                til.error = "密码长度为6位数"
                return@setOnClickListener
            }
            if (state) {
                LocalDataUtils.setString(
                    LocalDataUtils.LOCAL_PASSWORD_STATE,
                    BaseApplication.userId.toString() + " " +"close "+ text
                )

                toast("关闭成功")
            }else{
                LocalDataUtils.setString(
                    LocalDataUtils.LOCAL_PASSWORD_STATE,
                    BaseApplication.userId.toString() + " " +"open "+ text
                )

                toast("开启成功")
            }


            finish()

        }
    }

    override fun initData() {
    }
}