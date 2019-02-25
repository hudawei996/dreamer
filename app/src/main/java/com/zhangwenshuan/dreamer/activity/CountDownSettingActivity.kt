package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.GeneralAdapter
import com.zhangwenshuan.dreamer.adapter.GeneralStyle
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.Item
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import kotlinx.android.synthetic.main.activity_count_down_setting.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.jetbrains.anko.toast

class CountDownSettingActivity : BaseActivity() {
    private var list = mutableListOf<Item>()


    private lateinit var adapter: GeneralAdapter

    var target: String = ""

    override fun setResourceId(): Int {
        return R.layout.activity_count_down_setting
    }

    override fun preInitData() {

        list.add(
            Item(
                resources.getString(R.string.upload), "同步云端",
                subTitle = "同步本地数据到云端",
                style = GeneralStyle.STYLE_HAVE_HINT,
                iconColor = resources.getColor(R.color.chart_color_1), showRight = true
            )
        )

        list.add(
            Item(
                resources.getString(R.string.download), "同步本地",
                subTitle = "同步云端数据到本地",
                style = GeneralStyle.STYLE_HAVE_HINT,
                iconColor = resources.getColor(R.color.chart_color_4), showRight = true
            )
        )


        var countDownSate = "关"

        var countDownTarget = LocalDataUtils.getString(LocalDataUtils.COUNT_DOWN_TARGET)

        if (!countDownTarget.isEmpty()) {
            var data = countDownTarget.split("_dreamer_")

            if (data[0] == BaseApplication.token) {
                target = data[1]
                countDownSate = data[2]
            }
        }



        list.add(
            Item(
                resources.getString(R.string.show_splash), "启动显示",
                subTitle = "启动页展示首页目标倒计时",
                style = GeneralStyle.STYLE_HAVE_HINT_AND_VALUE,
                value = countDownSate,
                iconColor = resources.getColor(R.color.chart_color_5), showRight = true
            )
        )



        adapter = GeneralAdapter(this, list)

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        startActivity(Intent(this@CountDownSettingActivity, CountDownUploadActivity::class.java))
                    }
                    1 -> {
                        startActivity(Intent(this@CountDownSettingActivity, CountDownDownloadActivity::class.java))
                    }
                    2 -> {

                        if (target.isEmpty()) {
                            toast("未设置首页目标")
                            return
                        }


                        if (countDownSate == "开") {
                            list[1].value = "关"
                            countDownSate = "关"
                            LocalDataUtils.setString(
                                LocalDataUtils.COUNT_DOWN_TARGET,
                                BaseApplication.token + "_dreamer_" + target + "_dreamer_" + "关"
                            )
                        } else {
                            list[1].value = "开"
                            countDownSate = "开"
                            LocalDataUtils.setString(
                                LocalDataUtils.COUNT_DOWN_TARGET,
                                BaseApplication.token + "_dreamer_" + target + "_dreamer_" + "开"
                            )
                        }

                        list[2].value=countDownSate

                        adapter.notifyItemChanged(2)
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

    }

    override fun initData() {
    }
}