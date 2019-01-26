package com.zhangwenshuan.dreamer.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.v4.view.ViewPager
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.MainAdapter
import com.zhangwenshuan.dreamer.bean.Login
import com.zhangwenshuan.dreamer.fragment.*
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import com.zhangwenshuan.dreamer.util.SystemUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


class MainActivity : BaseActivity() {

    var fragments: MutableList<BaseFragment>? = null

    //底部要显示的标题
    var titles: MutableList<String>? = null

    //底部要显示的icon
    var icons: MutableList<String>? = null

    var adapter: MainAdapter? = null

    var statusHeight = 0
    var navigationHegiht = 0


    override fun setResourceId(): Int = R.layout.activity_main

    override fun preInitData() {
        EventBus.getDefault().register(this)

        fragments = mutableListOf(MainFragment(), CountDownTimeFragment(), MoreFragment(), MeFragment())


        titles = mutableListOf("梦想家", "倒计时", "发现", "我")

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

        vpMain.offscreenPageLimit = 4


        initBottomView()

        showPasswordDialog()



        vpMain.setCurrentItem(0, false)

        statusHeight = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

//        navigationHegiht =
//                resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))
//
//
//        val hasNavigation = SystemUtil.checkDeviceHasNavigationBar(this)
//
//        if (hasNavigation) {
//            navigationHegiht = SystemUtil.getNavigationBarHeight(this)
//
//        }

    }

    private fun showPasswordDialog() {
        val data = LocalDataUtils.getString(LocalDataUtils.LOCAL_PASSWORD_STATE)

        if (!data.isEmpty()) {
            val strs = data.split(" ")

            if (strs[0].toInt() == BaseApplication.userId) {
                if (strs[1] == "open") {
                    toShowPasswordDialogView(strs[2])
                }
            }
        }
    }

    private fun toShowPasswordDialogView(s: String) {

        val view = LayoutInflater.from(this).inflate(R.layout.layout_password_input, null, false)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            finish()

        }

        val etPassword = view.findViewById<EditText>(R.id.etPasswordConfirm)

        view.findViewById<TextView>(R.id.tvConfirm).setOnClickListener {
            if (etPassword.text.toString() == s) {
                dialog.dismiss()
            } else {
                etPassword.text = Editable.Factory.getInstance().newEditable("")
                toast("密码错误")
            }
        }

        dialog.show()

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
        window.statusBarColor = Color.TRANSPARENT

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        vpMain.post {
            fragments!![0].setStatusBarHeight(statusHeight)
        }


        vpMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    setStatusBarColor(false, Color.TRANSPARENT)

                } else if (position == 1) {
                    setStatusBarColor(true, Color.TRANSPARENT)

                } else if (position == 2) {
                    setStatusBarColor(true, resources.getColor(R.color.colorPrimary) )

                } else if (position == 3) {
                    setStatusBarColor(true, resources.getColor(R.color.colorPrimary))

                }
                if (fragments!=null){
                    fragments!![position].setStatusBarHeight(statusHeight)
                }
                vpMain.requestLayout()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


    }


    private fun setStatusBarColor(isLight: Boolean, color: Int) {

        window.statusBarColor = color

        if (isLight) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

    }


    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribe(login: Login) {
        if (login.type == 1) {
            toast("登录失效,重新登录")
        }

        LocalDataUtils.setString(LocalDataUtils.TOKEN, "")
        LocalDataUtils.setString(LocalDataUtils.AVATAE, "")
        startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


}
