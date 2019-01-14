package com.zhangwenshuan.dreamer.activity

import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.UpdateIntroduce
import com.zhangwenshuan.dreamer.bean.UpdateNickname
import com.zhangwenshuan.dreamer.bean.User
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_introduce.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class IntroduceActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_introduce
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

        if (user?.introduce != null) {
            etIntroduce.hint=user?.introduce

            etIntroduce.text = Editable.Factory.getInstance().newEditable(user?.introduce)
            tvCounter.text=(32 - user!!.introduce.length).toString()
        }


        etIntroduce.requestFocus()

    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            val name = etIntroduce.text.toString()

            if (name.isEmpty()) {
                toast("个性签名不能为空")
                return@setOnClickListener
            }

            NetUtils.data(NetUtils.getApiInstance().updateIntroduce(BaseApplication.userId, name), Consumer {
                toast(it.message)

                if (it.code == 200) {
                    user?.introduce = name
                    BaseApplication.setUserLocal(user!!)
                    EventBus.getDefault().post(UpdateIntroduce(name))
                    finish()
                }
            })

        }

        etIntroduce.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvCounter.text = (32 - s.toString().length).toString()
            }
        })
    }

    override fun initData() {
    }

}