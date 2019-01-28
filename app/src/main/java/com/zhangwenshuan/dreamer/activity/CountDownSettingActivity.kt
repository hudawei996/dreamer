package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.GeneralAdapter
import com.zhangwenshuan.dreamer.adapter.GeneralStyle
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.Item
import kotlinx.android.synthetic.main.activity_count_down_setting.*
import kotlinx.android.synthetic.main.layout_title_bar.*

class CountDownSettingActivity : BaseActivity() {
    private var list = mutableListOf<Item>()


    private lateinit var adapter: GeneralAdapter

    override fun setResourceId(): Int {
        return R.layout.activity_count_down_setting
    }

    override fun preInitData() {

        list.add(
            Item(
                resources.getString(R.string.sync), "同步",
                subTitle = "同步服务器数据",
                iconColor = resources.getColor(R.color.chart_color_4), showRight = true
            )
        )

        list.add(
            Item(
                resources.getString(R.string.show_splash), "启动显示",
                subTitle = "启动页展示首页目标倒计时",
                iconColor = resources.getColor(R.color.chart_color_5), showRight = true
            )
        )



        adapter = GeneralAdapter(this, GeneralStyle.STYLE_HAVE_HINT, list)

        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                when(position){
                    0->{

                    }
                }
            }
        })

    }

    override fun initViews() {
        tvSubtitle.text = "倒计时"

        tvTitle.visibility = View.GONE

        tvSubtitle.visibility = View.VISIBLE

        rvCountDownSetting.adapter = adapter

        rvCountDownSetting.layoutManager = LinearLayoutManager(this)

    }

    override fun initListener() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        startActivity(Intent(this@CountDownSettingActivity, PasswordActivity::class.java))
                    }

                }
            }
        })
    }

    override fun initData() {
    }
}