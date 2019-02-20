package com.zhangwenshuan.dreamer.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import java.text.DecimalFormat

var decimalFormat = DecimalFormat("#,##0.00")

class SystemUtil {

    companion object {
        //判断是否存在NavigationBar
        fun checkDeviceHasNavigationBar(context: Context): Boolean {
            var hasNavigationBar = false
            val rs = context.getResources()
            val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id)
            }
            try {
                val systemPropertiesClass = Class.forName("android.os.SystemProperties")
                val m = systemPropertiesClass.getMethod("get", String::class.java)
                val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
                if ("1" == navBarOverride) {
                    hasNavigationBar = false
                } else if ("0" == navBarOverride) {
                    hasNavigationBar = true
                }
            } catch (e: Exception) {

            }

            return hasNavigationBar

        }

        fun getNavigationBarHeight(context: Context): Int {
            val resources = context.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            //获取NavigationBar的高度
            val navigationHeight = resources . getDimensionPixelSize (resourceId)



            return navigationHeight
        }




        private var mChildOfContent: View? = null
        private var usableHeightPrevious: Int = 0
        private var frameLayoutParams: ViewGroup.LayoutParams? = null


        fun requestLayout(content: View){
            if (content != null) {
                mChildOfContent = content
                mChildOfContent!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        possiblyResizeChildOfContent()
                    }

                })
                frameLayoutParams = mChildOfContent!!.layoutParams
            }
        }

        private fun possiblyResizeChildOfContent() {
            val usableHeightNow = computeUsableHeight()
            if (usableHeightNow != usableHeightPrevious) {
                //如果两次高度不一致
                //将计算的可视高度设置成视图的高度
                frameLayoutParams!!.height = usableHeightNow;
                mChildOfContent!!.requestLayout()//请求重新布局
                usableHeightPrevious = usableHeightNow
            }
        }

        private fun computeUsableHeight():Int {
            //计算视图可视高度
            val r = Rect()
            mChildOfContent!!.getWindowVisibleDisplayFrame(r)
            return r.bottom
        }

    }


}