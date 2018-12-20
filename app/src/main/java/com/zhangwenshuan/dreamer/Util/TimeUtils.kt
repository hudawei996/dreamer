package com.zhangwenshuan.dreamer.util

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        fun dateToString(date: Date): String {

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm")

            return format.format(date)
        }

        fun curTime():String{
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm")

            return format.format(Date())
        }

        fun curMonth():String{
            val format = SimpleDateFormat("yyyy-MM")

            return format.format(Date())
        }

        fun curMonthFirstDay():String{
            val format = SimpleDateFormat("yyyy-MM-dd")

            val calendar=Calendar.getInstance()

            calendar.set(Calendar.DAY_OF_MONTH,1)

            return format.format(calendar.time)
        }

        fun curDay():String{
            val format = SimpleDateFormat("yyyy-MM-dd")

            return format.format(Date())
        }

        fun getDay(date: Date): String {
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.format(date)
        }



    }
}