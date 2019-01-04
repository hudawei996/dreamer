package com.zhangwenshuan.dreamer.bean

import java.io.Serializable

data class Bank(
    var id: Int = 0,
    var userId: Int = 0,
    var number: String? = null,
    var name: String? = null,
    var account: Double? = null,

    var remark: String? = null,

    var type:String,

    //账单日
    var billDate: String? = null,

    var returnDate: String? = null,

    var username:String?=null,

    var amount: Double = 0.toDouble(),

    var debt: Double = 0.toDouble()
) : Serializable
