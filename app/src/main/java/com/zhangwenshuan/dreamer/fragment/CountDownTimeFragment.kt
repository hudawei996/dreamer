package com.zhangwenshuan.dreamer.fragment

import com.zhangwenshuan.dreamer.R
import kotlinx.android.synthetic.main.fragment_me.*

class CountDownTimeFragment : BaseFragment() {
    override fun getLayoutResource(): Int {
        return R.layout.fragment_count_down_time
    }

    override fun preInitData() {

    }

    override fun initViews() {
        val statusHeight =
            resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

        vTop.layoutParams.height = statusHeight

    }

    override fun initListener() {

    }

    override fun initData() {

    }
}