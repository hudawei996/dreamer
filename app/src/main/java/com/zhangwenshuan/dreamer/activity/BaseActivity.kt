package com.zhangwenshuan.dreamer.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(setResourceId())

        preInitData()

        initViews()

        initListener()

        initData()

    }

    /**
     * 设置资源布局
     */
    abstract fun setResourceId(): Int
    /**
     * 在初始化视图前需要初始化的数据
     */
    abstract fun preInitData()

    /**
     * 初始化视图
     */
    abstract fun initViews()

    /**
     * 初始化Listener
     */
    abstract fun initListener()

    /**
     * 初始化数据
     */
    abstract fun initData()


}