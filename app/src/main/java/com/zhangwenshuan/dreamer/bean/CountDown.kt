package com.zhangwenshuan.dreamer.bean

data class CountDown(
    var id:Int,
    var name: String,
    var createTime: String,
    var beginTime: String,
    var endTime: String,
    var order: Int = 0,
    var final: Int = 0,
    var show:Int=0
)