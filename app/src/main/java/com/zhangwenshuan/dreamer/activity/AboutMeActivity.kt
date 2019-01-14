package com.zhangwenshuan.dreamer.activity

import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.util.DEBUG
import kotlinx.android.synthetic.main.activity_about_me.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.jetbrains.anko.toast

class AboutMeActivity : BaseActivity() {
    override fun initListener() {
        tvAbout.setOnClickListener {
            if (DEBUG) {
                toast("商用模式")
                DEBUG = false
            } else {
                toast("开发者模式")
                DEBUG = true

            }
        }
    }

    override fun setResourceId(): Int {
        return R.layout.activity_about_me
    }

    override fun preInitData() {
    }

    override fun initViews() {
        tvTitle.text = "关于我"
    }


    override fun initData() {
    }
}