package com.zhangwenshuan.dreamer.bean

import android.graphics.Color
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.GeneralStyle
import java.io.Serializable


/**
 * define bean
 */

data class Password(
    var name: String, var username: String, var password: String,

    var userId: Int,

    var id: Int?=0
)

data class Event(var code: Int, var emssage: String)

data class Logo(var type: LogoType, var name: String)

data class User(var username: String,var nickname:String,
                var avatarUrl:String?,
                var introduce:String,var sex:String, var id: Int?)

data class Finance(
    var id:Int?,
    var time: String,
    var account: Double,
    var bankName: String,
    var bankId: Int,
    var item:String,
    var type:String,
    var remark: String,
    var date: String,

    var isExpense:Int
):Serializable

data class Expense(var account: Double?, var time: String, var type: String, var remark: String)

data class Result<T>(var code: Int, var message: String, var data: T)

data class Account(
    var monthIncome: Double,
    var monthExpense: Double,
    var latelyWeekIncomeAccount: List<DayAccount>?,
    var latelyWeekExpenseAccount: List<DayAccount>?
)

data class TotalAccount(var incomeAccount:Double,var expenseAccount:Double)

data class DayAccount(var account: Double, var date: String)


data class Item(var icon:String, var name:String, var iconColor:Int= Color.BLUE,
                var subTitle:String="",var value:String="",
                var style:GeneralStyle=GeneralStyle.STYLE_NORMAL,
                var showRight:Boolean=false,var showTop:Boolean=false)


data class BankCard(var name: String, var account: Double, var number: String, var id: Int?) : RightBean(name),
    Serializable

/**
 * define enum
 */
enum class LogoType {
    AI_QI_YI, TEN_XUN, YOU_KU, WEI_BO, WIFI
}


fun getImageRes(type: LogoType): Int {
    when (type) {
        LogoType.AI_QI_YI -> return R.mipmap.ic_ai_qi_yi
        LogoType.TEN_XUN -> return R.mipmap.ic_tengxun
        LogoType.YOU_KU -> return R.mipmap.ic_youku
        LogoType.WEI_BO -> return R.mipmap.ic_weibo
        LogoType.WIFI -> return R.mipmap.ic_wifi
        else -> return R.mipmap.ic_launcher
    }


}

