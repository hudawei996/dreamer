package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import android.util.Log
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.BookListActivity
import com.zhangwenshuan.dreamer.activity.FinanceActivity
import com.zhangwenshuan.dreamer.activity.PasswordActivity
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {
    override fun getLayoutResource(): Int = R.layout.fragment_main

    override fun preInitData() {
        Log.i("dreamer", "pre init data")
    }

    override fun initViews() {

    }

    override fun initListener() {
        rlPassword.setOnClickListener {
            startActivity(Intent(activity, PasswordActivity::class.java))
        }

        rlFinance.setOnClickListener {
            startActivity(Intent(activity, FinanceActivity::class.java))

        }

        rlBook.setOnClickListener {
            startActivity(Intent(activity, BookListActivity::class.java))

        }
    }

    override fun initData() {

    }
}