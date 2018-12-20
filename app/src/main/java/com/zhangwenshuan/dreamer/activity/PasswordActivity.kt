package com.zhangwenshuan.dreamer.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.fragment.PasswordListFragment
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_password.*
import org.jetbrains.anko.toast

class PasswordActivity : BaseActivity() {


    override fun setResourceId(): Int {
        return R.layout.activity_password
    }

    override fun preInitData() {

    }


    override fun initViews() {
        findViewById<View>(android.support.v7.appcompat.R.id.search_plate).setBackground(null)
        findViewById<View>(android.support.v7.appcompat.R.id.submit_area).setBackground(null)

        passwordListFragment = PasswordListFragment()
        supportFragmentManager.beginTransaction().add(R.id.flPasswordContent, passwordListFragment!!).commit()
    }

    override fun initListener() {
        ivAdd.setOnClickListener {
            startActivity(Intent(this@PasswordActivity, PasswordAddActivity::class.java))
        }


        svPassword.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {

                Log.e("dreamer", "close")

                return true
            }
        })




        svPassword.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    toSearchPassword(query!!)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length == 0) {
                       passwordListFragment.showAllPassword()

                    closeSoftInput()
                }
                return false
            }

        })
    }

    private fun toSearchPassword(passwordName: String) {
        NetUtils.data(NetUtils.getApiInstance().searchPassword(passwordName, BaseApplication.userId), Consumer {
            if (it.code == 200) {
                passwordListFragment.searchPassword(it.data)
            }
            toast(it.message)
        })
    }

    lateinit var passwordListFragment: PasswordListFragment

    override fun initData() {

    }


    fun closeSoftInput() {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        manager.hideSoftInputFromWindow(svPassword.windowToken, 0)
    }
}

