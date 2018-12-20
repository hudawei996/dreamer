package com.zhangwenshuan.dreamer.bean

import java.io.Serializable

data class Book(var id: Int?, var userId: Int,
                var name:String,
                var content: String, var begin:String,
                var end:String,var evaluate: String):Serializable