package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zhangwenshuan.dreamer.fragment.BaseFragment

class MainAdapter(val context: Context, val list: MutableList<BaseFragment>,val title:MutableList<String> ,manager: FragmentManager) :
    FragmentPagerAdapter(manager) {
    override fun getItem(p0: Int): Fragment=list[p0]

    override fun getCount(): Int =list.size

    override fun getPageTitle(position: Int): CharSequence?=title[position]
}