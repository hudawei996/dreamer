package com.zhangwenshuan.dreamer.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.support.v4.view.ViewPager
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.MainAdapter
import com.zhangwenshuan.dreamer.bean.Login
import com.zhangwenshuan.dreamer.fragment.BaseFragment
import com.zhangwenshuan.dreamer.fragment.CountDownTimeFragment
import com.zhangwenshuan.dreamer.fragment.MainFragment
import com.zhangwenshuan.dreamer.fragment.MeFragment
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.LocalDataUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_password_input.*
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

    override fun setResourceId(): Int = R.layout.activity_main

    override fun preInitData() {
        EventBus.getDefault().register(this)

        fragments = mutableListOf(MainFragment(),CountDownTimeFragment(), MeFragment())


        titles = mutableListOf("梦想家","倒计时", "我")

        icons = mutableListOf(
            resources.getString(R.string.home),
            resources.getString(R.string.timeline),
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

        vpMain.setCurrentItem(1,false)

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

    }

    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribe(login: Login) {
        if (login.type == 1) {
            toast("登录失效,重新登录")
        }

        LocalDataUtils.setString(LocalDataUtils.TOKEN, "")
        LocalDataUtils.setString(LocalDataUtils.AVATAE,"")
        startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


}
