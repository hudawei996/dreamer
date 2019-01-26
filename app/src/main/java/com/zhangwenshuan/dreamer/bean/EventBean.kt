package com.zhangwenshuan.dreamer.bean

import android.os.Bundle

const val EVENT_SAVE_INCOME = 1001
const val EVENT_DELETE_INCOME = 1001
const val EVENT_SAVE_EXPENSE = 1011
const val EVENT_DELETE_EXPENSE = 1012

data class EventBean(var flag: Int, var message: String?, var bundle: Bundle?=null)


data class RemoveBank(var account:Double?=0.0)

data class PasswordAdd(var account:Double?=0.0)

data class BookAdd(var account:Double?=0.0)

data class FinanceDelete(var finance: Finance)

data class FinanceAdd(var finance: Finance)
data class FinanceUpdate(var finance: Finance)


data class BudgetAdd(var budgetBean: BudgetBean)

data class BankUpdate(var bank: Bank)

data class BankAdd(var bank: BankCard)

data class CashAdd(var bank: Bank)

data class CreditAdd(var bank: Bank)

data class BankAdd1(var bank: Bank)

data class MobileAdd(var bank: Bank)

data class TargetAdd(var type: Int=0)

data class TargetFirst(var countDown: CountDown)
data class TargetRemove(var type: Int=0)


data class BankDelete(var bank: BankCard)

data class Login(var type:Int=0)

data class UpdateNickname(var name:String)

data class UpdateIntroduce(var introduce:String)

data class AvatarUpload(var introduce:String="")