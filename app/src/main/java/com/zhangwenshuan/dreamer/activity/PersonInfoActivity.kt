package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.UpdateNickname
import com.zhangwenshuan.dreamer.util.BaseApplication
import kotlinx.android.synthetic.main.activity_person_info.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PersonInfoActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_person_info
    }

    override fun preInitData() {
        EventBus.getDefault().register(this)
    }

    override fun initViews() {
        tvTitle.text = "个人信息"
        val typeface = Typeface.createFromAsset(assets, "icon_action.ttf")
        tvRight1.typeface = typeface
        tvRight2.typeface = typeface
        tvRight3.typeface = typeface


        val user = BaseApplication.user

        if (user?.nickname != null) {
            tvNickname.text = user?.nickname
        }

        if (user?.introduce != null) {
            tvIntroduce.text = user?.introduce
        }
    }

    override fun initListener() {

        tvRight1.setOnClickListener {
            tvNickname.performClick()
        }


        tvNickname.setOnClickListener {
            startActivity(Intent(this@PersonInfoActivity, NicknameActivity::class.java))
        }
    }

    override fun initData() {
    }

    @Subscribe
    fun subscribe(update:UpdateNickname){
        tvNickname.text=update.name
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}