package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.SettingActivity
import kotlinx.android.synthetic.main.fragment_me.*

class MeFragment:BaseFragment() {
    override fun getLayoutResource(): Int= R.layout.fragment_me

    override fun preInitData() {
    }

    override fun initViews() {
    }

    override fun initListener() {
        rlSetting.setOnClickListener {
            startActivity(Intent(activity,SettingActivity::class.java))
        }
    }

    override fun initData() {
    }
}