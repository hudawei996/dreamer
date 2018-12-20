package com.zhangwenshuan.dreamer.fragment

import android.app.AlertDialog
import android.util.Log
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.PasswordActivity
import com.zhangwenshuan.dreamer.adapter.PasswordListAdapter
import com.zhangwenshuan.dreamer.bean.Password
import com.zhangwenshuan.dreamer.bean.PasswordAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TAG
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_password_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class PasswordListFragment : BaseFragment() {

    lateinit var list: MutableList<Password>

    lateinit var adapter: PasswordListAdapter


    var context: PasswordActivity? = null


    private var deletePasswordPosition = 0


    override fun getLayoutResource(): Int {
        return R.layout.fragment_password_list
    }

    override fun preInitData() {
        EventBus.getDefault().register(this)

        context = activity as PasswordActivity

        list = mutableListOf()

        adapter = PasswordListAdapter(activity!!, list)

    }

    override fun initViews() {
        lvPasswordList.adapter = adapter

        adapter.notifyDataSetChanged()

    }

    override fun initListener() {
        lvPasswordList.setOnItemLongClickListener { parent, view, position, id ->

            deletePasswordPosition = position

            showDialog()

            true
        }


    }

    override fun initData() {
        toGetPassword()
    }


    private fun showDialog() {
        AlertDialog.Builder(activity).setMessage("确定删除密码？")
            .setPositiveButton("确定") { dialog, which -> toDelete();dialog.dismiss() }
            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun toDelete() {
        NetUtils.data(NetUtils.getApiInstance().deletePassword(list[deletePasswordPosition].id!!), Consumer {
            context?.toast(it.message)

            if (it.code == 200) {
                list.removeAt(deletePasswordPosition)
                adapter.notifyDataSetChanged()
                notifyViewChanged()
            }
        })


        notifyViewChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeAdd(password: PasswordAdd) {
        toGetPassword()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    private fun toGetPassword() {
        NetUtils.data(NetUtils.getApiInstance().getAllPassword(BaseApplication.userId), Consumer {
            if (it.code == 200) {
                list.clear()
                list.addAll(it.data)
            } else {
                context?.toast(it.message)
            }

            notifyViewChanged()
        })


    }

    private fun notifyViewChanged() {
        if (list.size == 0) {
            tvNoPassword.visibility = View.VISIBLE
            lvPasswordList.visibility = View.GONE
        } else {
            tvNoPassword.visibility = View.GONE
            lvPasswordList.visibility = View.VISIBLE
        }


        Log.i(TAG, list.toString())

        adapter.notifyDataSetChanged()
    }

    fun searchPassword(data: List<Password>) {
        list.clear()

        list.addAll(data)


        notifyViewChanged()
    }

    fun showAllPassword() {
        toGetPassword()
    }


}