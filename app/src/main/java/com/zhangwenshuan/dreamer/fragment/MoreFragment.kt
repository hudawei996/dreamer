package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.BookListActivity
import com.zhangwenshuan.dreamer.activity.PasswordActivity
import com.zhangwenshuan.dreamer.adapter.MoreAdapter
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.Item
import com.zhangwenshuan.dreamer.util.logInfo
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : BaseFragment() {
    override fun getLayoutResource(): Int {
        return R.layout.fragment_more
    }

    override fun setStatusBarHeight(height: Int) {

        if (vTop!=null){
            var layoutParams = vTop.layoutParams

            var oldHeight = layoutParams.height

            logInfo(oldHeight.toString())

            layoutParams.height = height

            vTop.layoutParams = layoutParams
        }
    }


    private var list = mutableListOf<Item>()

    private lateinit var adapter: MoreAdapter

    override fun preInitData() {
        list.add(Item(activity!!.resources.getString(R.string.password_remember), "密码记忆", showRight = true))
        list.add(Item(activity!!.resources.getString(R.string.book_remember), "书架", showRight = true,showTop = true))
        list.add(Item(activity!!.resources.getString(R.string.remember), "备忘录", showRight = true,showTop = true))

        adapter = MoreAdapter(activity!!, list)


    }

    override fun initViews() {

        rvMore.adapter = adapter

        rvMore.layoutManager = LinearLayoutManager(activity!!)
    }

    override fun initListener() {
        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                when(position){
                    0->{
                        startActivity(Intent(activity,PasswordActivity::class.java))
                    }
                    1->{
                        startActivity(Intent(activity,BookListActivity::class.java))
                    }
                }
            }
        })
    }

    override fun initData() {

    }
}