package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.UpdateNickname
import com.zhangwenshuan.dreamer.bean.User
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_nickname.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class NicknameActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_nickname
    }

    override fun preInitData() {
    }

    var user: User? = null

    override fun initViews() {
        tvTitle.visibility = View.GONE

        tvSubtitle.visibility = View.VISIBLE

        tvSubtitle.text = "个人信息"

        tvAdd.visibility = View.VISIBLE

        tvAdd.text = resources.getText(R.string.ok)

        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

        user = BaseApplication.user

        if (user?.nickname != null) {
            etNickname.text = Editable.Factory.getInstance().newEditable(user?.nickname)
        }

        etNickname.requestFocus()

    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            val name = etNickname.text.toString()

            if (name.isEmpty()) {
                toast("名字不能为空")
                return@setOnClickListener
            }

            NetUtils.data(NetUtils.getApiInstance().updateNickname(BaseApplication.userId, name), Consumer {
                toast(it.message)

                if (it.code == 200) {
                    user?.nickname = name
                    BaseApplication.setUserLocal(user!!)
                    EventBus.getDefault().post(UpdateNickname(name))
                    finish()
                }
            })

        }
    }

    override fun initData() {
    }
}