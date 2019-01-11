package com.zhangwenshuan.dreamer.activity

import com.zhangwenshuan.dreamer.R
import kotlinx.android.synthetic.main.layout_title_bar.*

class AboutMeActivity:BaseActivity() {
    override fun initListener() {

    }

    override fun setResourceId(): Int {
        return R.layout.activity_about_me
    }

    override fun preInitData() {
    }

    override fun initViews() {
        tvTitle.text="关于我"
    }


    override fun initData() {
    }
}