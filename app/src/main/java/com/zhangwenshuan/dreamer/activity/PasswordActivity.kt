package com.zhangwenshuan.dreamer.activity

import android.view.View
import android.widget.SearchView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.fragment.PasswordListFragment

class PasswordActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_password
    }

    override fun preInitData() {

    }

    override fun initViews() {
        findViewById<View>(android.support.v7.appcompat.R.id.search_plate).setBackground(null)
        findViewById<View>(android.support.v7.appcompat.R.id.submit_area).setBackground(null)

        supportFragmentManager.beginTransaction().add(R.id.flPasswordContent,PasswordListFragment()).commit()
    }

    override fun initListener() {

    }

    override fun initData() {

    }
}
