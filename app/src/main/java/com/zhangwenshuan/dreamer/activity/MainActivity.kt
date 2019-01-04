package com.zhangwenshuan.dreamer.activity

import android.graphics.Typeface
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.MainAdapter
import com.zhangwenshuan.dreamer.bean.Login
import com.zhangwenshuan.dreamer.fragment.BaseFragment
import com.zhangwenshuan.dreamer.fragment.MainFragment
import com.zhangwenshuan.dreamer.fragment.MeFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseActivity() {

    var fragments: MutableList<BaseFragment>? = null

    //底部要显示的标题
    var titles: MutableList<String>? = null

    //底部要显示的icon
    var icons: MutableList<String>? = null

    var adapter: MainAdapter? = null

    override fun setResourceId(): Int = R.layout.activity_main

    override fun preInitData() {
        EventBus.getDefault().register(this)

        fragments = mutableListOf(MainFragment(), MeFragment())


        titles = mutableListOf("梦想家", "我")

        icons = mutableListOf(
            resources.getString(R.string.home),
            resources.getString(R.string.me)
        )

        adapter = MainAdapter(this, fragments!!, titles!!, supportFragmentManager)


    }

    override fun initViews() {


        vpMain.adapter = adapter

        tlMain.setupWithViewPager(vpMain)

        vpMain.offscreenPageLimit = 4


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

        vpMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                tvTabName.text = titles!![p0]
            }
        })
    }

    override fun initListener() {

    }

    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribe(login: Login){
        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


}
