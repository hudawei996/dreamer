package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import com.bumptech.glide.Glide
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.*
import com.zhangwenshuan.dreamer.bean.AvatarUpload
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

    override fun setStatusBarHeight(height: Int) {
        if (vTop!=null){
            var layoutParams = vTop.layoutParams

            layoutParams.height = height

            vTop.layoutParams = layoutParams
        }
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

        if (BaseApplication.avatar.isEmpty()){
            Glide.with(activity!!).asBitmap().load(R.mipmap.img_logo).into(ivHeader)
        }else{
            Glide.with(activity!!).asBitmap().load(BaseApplication.avatar).apply(glideOptions).into(ivHeader)
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

        rlCountDown.setOnClickListener {
            startActivity(Intent(activity, CountDownSettingActivity::class.java))

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

 @Subscribe
    fun subscribe(update: AvatarUpload) {
     Glide.with(activity!!).asBitmap().load(BaseApplication.avatar).apply(glideOptions).into(ivHeader)
 }


    override fun initData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}