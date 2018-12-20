package com.zhangwenshuan.dreamer.activity

import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.fragment.MainFragment

class MainActivityv1 : BaseActivity() {

    override fun setResourceId(): Int {
        return R.layout.activity_main_v1
    }

    override fun preInitData() {
        supportFragmentManager.beginTransaction().add(R.id.flContent, MainFragment()).commit()
    }

    override fun initViews() {
    }

    override fun initListener() {
    }

    override fun initData() {

    }
}