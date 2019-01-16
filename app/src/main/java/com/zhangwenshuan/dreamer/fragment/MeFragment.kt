package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.*
import com.zhangwenshuan.dreamer.bean.UpdateIntroduce
import com.zhangwenshuan.dreamer.bean.UpdateNickname
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.logError
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MeFragment : BaseFragment() {
    override fun getLayoutResource(): Int = R.layout.fragment_me

    override fun preInitData() {
        EventBus.getDefault().register(this)

    }

    override fun beforeViewCreated() {
        super.beforeViewCreated()
        change()
    }

    private fun change() {

        val statusHeight =
            resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

        vTop.layoutParams.height = statusHeight

    }

    override fun initViews() {

        val user = BaseApplication.user

        logError(user.toString())


        if (user?.nickname != null) {
            tvNickname.text = user?.nickname
        }

        if (user?.introduce != null) {
            tvIntroduce.text = user?.introduce
        }

    }

    override fun initListener() {
        rlSetting.setOnClickListener {
            startActivity(Intent(activity, SettingActivity::class.java))
        }

        rlContact.setOnClickListener {
            startActivity(Intent(activity, ContactMeActivity::class.java))
        }

        rlAboutOur.setOnClickListener {
            startActivity(Intent(activity, AboutMeActivity::class.java))
        }

        rlFeedback.setOnClickListener {

            startActivity(Intent(activity, FeedbackActivity::class.java))
        }

        rlPerson.setOnClickListener {

            startActivity(Intent(activity, PersonInfoActivity::class.java))
        }
    }

    @Subscribe
    fun subscribe(update: UpdateNickname) {
        tvNickname.text = update.name
    }

    @Subscribe
    fun subscribe(update: UpdateIntroduce) {
        tvIntroduce.text = update.introduce
    }


    override fun initData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}