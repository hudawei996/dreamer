package com.zhangwenshuan.dreamer.activity

import com.zhangwenshuan.dreamer.R
import kotlinx.android.synthetic.main.layout_title_bar.*

abstract class FinanceBaseActivity : BaseActivity() {
    override fun preInitData() {
        if (llTitleBar != null) {
            llTitleBar.setBackgroundResource(R.color.finance_base_color)
            window.statusBarColor = resources.getColor(R.color.finance_base_color)
        }
    }
}