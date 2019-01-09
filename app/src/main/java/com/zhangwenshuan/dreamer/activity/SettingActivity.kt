package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.adapter.SettingAdapter
import com.zhangwenshuan.dreamer.bean.Login
import com.zhangwenshuan.dreamer.bean.LoginBean
import com.zhangwenshuan.dreamer.bean.Setting
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus

class SettingActivity : BaseActivity() {

    val list = mutableListOf<Setting>()

    lateinit var adapter: SettingAdapter

    override fun setResourceId(): Int {
        return R.layout.activity_setting
    }

    override fun preInitData() {

        tvTitle.text = "设置"

        list.add(Setting(resources.getString(R.string.password), "开屏密码", showTopLine = true))
        list.add(Setting(resources.getString(R.string.password), "修改登录密码", showTopLine = true))
        list.add(Setting(resources.getString(R.string.exit), "退出登录", showRight = false, showTopLine = true))
        adapter = SettingAdapter(this, list)

        rvSetting.adapter = adapter

        rvSetting.layoutManager = LinearLayoutManager(this)


    }

    override fun initViews() {

    }

    override fun initListener() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    list.size - 1 -> {
                        EventBus.getDefault().post(Login())
                        finish()
                    }
                    0 -> {
                        startActivity(Intent(this@SettingActivity, SettingPasswordActivity::class.java))
                    }

                    1 -> {
                        startActivity(Intent(this@SettingActivity, SettingUpdatePasswordActivity::class.java))
                    }

                }
            }
        })
    }

    override fun initData() {

    }
}