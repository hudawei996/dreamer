package com.zhangwenshuan.dreamer.activity

import android.graphics.Typeface
import com.zhangwenshuan.dreamer.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_title_bar.*


class RegisterActivity : BaseActivity() {
    override fun setResourceId(): Int = R.layout.activity_register

    override fun preInitData() {

    }

    override fun initViews() {
        tvTitle.text = "注册梦想家"

        val typeface = Typeface.createFromAsset(assets, "title.ttf")

        tvLogo.typeface = typeface

    }

    override fun initListener() {
        ivBack.setOnClickListener { finish() }
    }

    override fun initData() {
    }


}