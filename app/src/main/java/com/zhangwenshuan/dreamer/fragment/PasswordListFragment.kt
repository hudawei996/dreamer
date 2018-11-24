package com.zhangwenshuan.dreamer.fragment

import android.app.AlertDialog
import android.database.sqlite.SQLiteDatabase
import android.nfc.Tag
import android.util.Log
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.PasswordActivity
import com.zhangwenshuan.dreamer.activity.cursorToPassword
import com.zhangwenshuan.dreamer.adapter.PasswordListAdapter
import com.zhangwenshuan.dreamer.bean.Event
import com.zhangwenshuan.dreamer.bean.Password
import com.zhangwenshuan.dreamer.database.DatabaseHelper
import com.zhangwenshuan.dreamer.util.TAG
import com.zhangwenshuan.dreamer.util.userId
import kotlinx.android.synthetic.main.fragment_password_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PasswordListFragment : BaseFragment() {

    lateinit var list: MutableList<Password>

    lateinit var adapter: PasswordListAdapter


    var context:PasswordActivity?=null



    var curPosition = 0


    override fun getLayoutResource(): Int {
        return R.layout.fragment_password_list
    }

    override fun preInitData() {
        context=activity as PasswordActivity

        list = mutableListOf()

        adapter = PasswordListAdapter(activity!!, list)

        EventBus.getDefault().register(this)
    }

    override fun initViews() {
        lvPasswordList.adapter = adapter

        adapter.notifyDataSetChanged()

    }

    override fun initListener() {
        lvPasswordList.setOnItemLongClickListener { parent, view, position, id ->

            curPosition = position

            showDialog()

            true
        }


    }

    override fun initData() {


        getDataFormLocal()
    }


    private fun showDialog() {
        AlertDialog.Builder(activity).setMessage("确定删除密码？")
            .setPositiveButton("确定") { dialog, which -> toDelete();dialog.dismiss() }
            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun toDelete() {
        val result = context!!.writer.delete(DatabaseHelper.PASSWORD_TABLE, " id=?", arrayOf("${list[curPosition].id}"))
        Log.e(TAG, "${result}")
        if (result > 0) {
            list.removeAt(curPosition)
            
            notifyViewChanged()
        } else {

        }
    }

    @Subscribe
    fun update(event: Event) {
        if (event.code == 0) {
            getDataFormLocal()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun getDataFormLocal() {
        val cursor = context!!.reader.rawQuery(
            "select * from " +
                    "${DatabaseHelper.PASSWORD_TABLE} where user_id=${userId} order by id desc",
            null, null
        )


        list.clear()


       list.addAll(cursorToPassword(cursor))

        cursor.close()

        notifyViewChanged()

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

    fun searchPassword(data: MutableList<Password>) {
        list.clear()

        list.addAll(data)

        notifyViewChanged()
    }

    fun showAllPassword() {
        getDataFormLocal()
    }

}