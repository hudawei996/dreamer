package com.zhangwenshuan.dreamer.bean

data class DayBill(var income: Double=0.0, var expense: Double=0.0, var date:String="",var list: MutableList<Finance>?= mutableListOf())