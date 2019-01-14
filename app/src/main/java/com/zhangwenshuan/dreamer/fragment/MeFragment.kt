package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import android.graphics.Typeface
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

    override fun initViews() {
        val typeface = Typeface.createFromAsset(activity!!.assets, "icon_action.ttf")

        tvAboutMe.text = resources.getText(R.string.about)
        tvContact.text = resources.getText(R.string.contact)
        tvFeedback.text = resources.getText(R.string.feedback)
        tvAboutMe.text = resources.getText(R.string.about)
        tvSettings.text = resources.getText(R.string.setting)

        tvAboutMe.typeface = typeface
        tvContact.typeface = typeface
        tvFeedback.typeface = typeface
        tvSettings.typeface = typeface


        val user=BaseApplication.user

        logError(user.toString())


        if (user?.nickname!=null){
            tvNickname.text=user?.nickname
        }

        if (user?.introduce!=null){
            tvIntroduce.text=user?.introduce
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