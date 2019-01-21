package com.zhangwenshuan.dreamer.fragment

import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.util.logInfo
import kotlinx.android.synthetic.main.fragment_count_down_time.*

class CountDownTimeFragment : BaseFragment() {
    override fun getLayoutResource(): Int {
        return R.layout.fragment_count_down_time
    }

    override fun setStatusBarHeight(height: Int) {
        if (vTop!=null){
            var layoutParams = vTop.layoutParams

            var oldHeight = layoutParams.height

            logInfo(oldHeight.toString())


            layoutParams.height=height

            vTop.layoutParams=layoutParams
        }

    }

    override fun preInitData() {

    }

    override fun initViews() {


    }

    override fun initListener() {

    }

    override fun initData() {

    }
}