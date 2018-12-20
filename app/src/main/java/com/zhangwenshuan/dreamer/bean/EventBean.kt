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