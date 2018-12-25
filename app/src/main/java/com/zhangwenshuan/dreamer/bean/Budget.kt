package com.zhangwenshuan.dreamer.bean

import java.io.Serializable

data class Budget(var icon: Int, var name: String, var account: Double = 0.0)

class BudgetBean:Serializable {
    var id: Int = 0
    var userId: Int = 0
    var account: Double = 0.toDouble()
    var eat: Double = 0.toDouble()
    var transportation: Double = 0.toDouble()
    var shopping: Double = 0.toDouble()
    var amusement: Double = 0.toDouble()
    var medical: Double = 0.toDouble()
    var house: Double = 0.toDouble()
    var investment: Double = 0.toDouble()
    var social: Double = 0.toDouble()
    var business: Double = 0.toDouble()
    var month: String? = null
}
