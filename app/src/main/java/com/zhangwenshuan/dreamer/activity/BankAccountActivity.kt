package com.zhangwenshuan.dreamer.activity

import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankAccountAdapter
import com.zhangwenshuan.dreamer.bean.BankCard
import com.zhangwenshuan.dreamer.util.getBankCarsFromLocal
import kotlinx.android.synthetic.main.activity_bank_account.*
import kotlinx.android.synthetic.main.activity_finance.*

class BankAccountActivity : BaseActivity() {

    var list = mutableListOf<BankCard>()


    lateinit var adapter: BankAccountAdapter


    override fun setResourceId(): Int {
        return R.layout.activity_bank_account
    }

    override fun preInitData() {
        var list = getBankCarsFromLocal()

        adapter= BankAccountAdapter(this,list!!)

        lvBankAccount.adapter=adapter

        tvTitle.text="账户"

    }

    override fun initViews() {
    }

    override fun initListener() {
    }

    override fun initData() {
    }
}