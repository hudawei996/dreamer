package com.zhangwenshuan.dreamer.bean

import java.util.*

data class Countdown(
    var id: Int,
    var target: String,
    var createdTime: String,
    var beginTime: String,
    var endTime: String,
    var order: Int = 0,
    var success: Int = 0,
    var show: Int = 0,
    var userId: Int = 0,
    var uploadTime:Date?=null
)