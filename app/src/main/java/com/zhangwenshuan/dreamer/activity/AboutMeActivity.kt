package com.zhangwenshuan.dreamer.activity

import com.zhangwenshuan.dreamer.R
import kotlinx.android.synthetic.main.layout_title_bar.*
import android.R.attr.versionCode
import android.R.attr.versionName
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import kotlinx.android.synthetic.main.activity_about_me.*
import org.jetbrains.anko.ctx


class AboutMeActivity : BaseActivity() {
    override fun initListener() {

    }

    override fun setResourceId(): Int {
        return R.layout.activity_about_me
    }

    override fun preInitData() {

    }

    override fun initViews() {
        tvTitle.text = "关于"

        tvVersions.text=getVersionName()


    }

    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    fun getVersionName(): String {
        var appVersionName = ""
        val manager = packageManager
        try {
            val info = manager.getPackageInfo(packageName, 0)
            appVersionName = info.versionName // 版本名
           val currentVersionCode = info.versionCode // 版本号
            println(currentVersionCode.toString() + " " + appVersionName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return appVersionName
    }


    override fun initData() {
    }
}