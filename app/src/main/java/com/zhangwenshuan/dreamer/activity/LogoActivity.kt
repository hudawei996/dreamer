package com.zhangwenshuan.dreamer.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.LogoAdapter
import com.zhangwenshuan.dreamer.bean.Logo
import com.zhangwenshuan.dreamer.bean.LogoType
import com.zhangwenshuan.dreamer.bean.Result
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.Observer
import kotlinx.android.synthetic.main.activity_password_icon.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.reactivestreams.Subscriber

class LogoActivity : BaseActivity() {

    lateinit var list: MutableList<Logo>

    lateinit var adapter: LogoAdapter

    override fun setResourceId(): Int = R.layout.activity_password_icon

    override fun preInitData() {

        val aiqiyi = Logo(LogoType.AI_QI_YI, "爱奇艺")
        val tenxun = Logo(LogoType.TEN_XUN, "腾讯")
        val wifi = Logo(LogoType.WIFI, "WIFI")
        val weibo = Logo(LogoType.WEI_BO, "微博")
        val youku = Logo(LogoType.YOU_KU, "优酷")

        list = mutableListOf(wifi, tenxun, weibo, aiqiyi, youku)

        adapter = LogoAdapter(this, list)

    }

    override fun initViews() {

        tvTitle.text="Logo"

        gvPasswordLogo.adapter = adapter


    }

    override fun initListener() {


        gvPasswordLogo.setOnItemClickListener { parent, view, position, id ->
           val intent= Intent()
            intent.putExtra("data",list[position].type.ordinal)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }

    override fun initData() {

    }


}