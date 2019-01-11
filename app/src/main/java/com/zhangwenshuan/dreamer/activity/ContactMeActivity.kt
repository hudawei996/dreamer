package com.zhangwenshuan.dreamer.activity

import com.zhangwenshuan.dreamer.R
import kotlinx.android.synthetic.main.layout_title_bar.*

class ContactMeActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_contact_me
    }

    override fun preInitData() {
    }

    override fun initViews() {
        tvTitle.text="联系我"
    }

    override fun initListener() {
    }

    override fun initData() {
    }


}