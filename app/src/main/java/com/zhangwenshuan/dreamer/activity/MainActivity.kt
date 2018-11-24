package com.zhangwenshuan.dreamer.activity

import android.text.Layout
import android.view.LayoutInflater
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.MainAdapter
import com.zhangwenshuan.dreamer.fragment.BaseFragment
import com.zhangwenshuan.dreamer.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Typeface


class MainActivity : BaseActivity() {

    var fragments: MutableList<BaseFragment>? = null

    //底部要显示的标题
    var titles: MutableList<String>? = null

    //底部要显示的icon
    var icons: MutableList<String>? = null

    var adapter: MainAdapter? = null

    override fun setResourceId(): Int = R.layout.activity_main

    override fun preInitData() {
        fragments = mutableListOf(MainFragment(), MainFragment(), MainFragment(), MainFragment())


        titles = mutableListOf("首页", "时间轴", "发现", "我的")

        icons = mutableListOf(
            resources.getString(R.string.home),
            resources.getString(R.string.timeline),
            resources.getString(R.string.found),
            resources.getString(R.string.me)
        )

        adapter = MainAdapter(this, fragments!!, titles!!, supportFragmentManager)


    }

    override fun initViews() {


        vpMain.adapter = adapter

        tlMain.setupWithViewPager(vpMain)


        initBottomView()

    }


    /**
     * 初始底部的导航栏视图
     */
    private fun initBottomView() {

        val iconFont = Typeface.createFromAsset(assets, "tab.ttf")

        for ((index) in fragments!!.withIndex()) {
            val view = LayoutInflater.from(this).inflate(R.layout.layout_main_tab, null, false)

            val tvHome = view.findViewById<TextView>(R.id.tvIcon)

            tvHome.text = icons!![index]


            tvHome.typeface = iconFont

            val tvText = view.findViewById<TextView>(R.id.tvText)

            tvText.text = titles!![index]

            tlMain.getTabAt(index)!!.customView = view
        }


    }

    override fun initListener() {
    }

    override fun initData() {
    }


}
