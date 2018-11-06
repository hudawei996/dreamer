package com.zhangwenshuan.dreamer.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(getLayoutResource(),container,false)
    }

    abstract fun getLayoutResource(): Int


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preInitData()

        initViews()

        initListener()

        initData()
    }

    abstract fun preInitData()

    abstract fun initViews()

    abstract fun initListener()

    abstract fun initData()
}