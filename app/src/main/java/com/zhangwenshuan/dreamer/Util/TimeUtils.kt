package com.zhangwenshuan.dreamer.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        fun dateToString(date: Date): String {

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm")

            return format.format(date)
        }

        fun curTime(): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm")

            return format.format(Date())
        }

        fun curMonth(): String {
            val format = SimpleDateFormat("yyyy-MM")

            return format.format(Date())
        }

        fun curMonthFirstDay(): String {
            val format = SimpleDateFormat("yyyy-MM-dd")

            val calendar = Calendar.getInstance()

            calendar.set(Calendar.DAY_OF_MONTH, 1)

            return format.format(calendar.time)
        }

        fun curDay(): String {
            val format = SimpleDateFormat("yyyy-MM-dd")

            return format.format(Date())
        }

        fun getDay(date: Date): String {
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.format(date)
        }

        fun toTimestamp(date: String): Long {
            val format = SimpleDateFormat("yyyy-MM-dd")

            val date = format.parse(date)

            logInfo(date.time.toString())

            return date.time
        }


        fun timeDifference(start: Long, end: Long): MutableList<Long> {

            val between = end - start
            var day = between / (24 * 60 * 60 * 1000);
            var hour = (between / (60 * 60 * 1000) - day * 24);
            var min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
            var s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            var ms = (
                    between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                            - min * 60 * 1000 - s * 1000
                    )
            var timeDifference = day.toString() + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒"

            logInfo(timeDifference)

            return mutableListOf(day,hour,min,s,ms)
        }


        fun closeInput(context: Activity) {

            var imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            if (imm != null) {
                imm.hideSoftInputFromWindow(context.window.decorView.windowToken, 0)
            }
        }
    }


}