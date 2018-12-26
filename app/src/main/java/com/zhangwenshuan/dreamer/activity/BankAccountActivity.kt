package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankAccountAdapter
import com.zhangwenshuan.dreamer.bean.BankCard
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_bank_account.*
import kotlinx.android.synthetic.main.layout_title_bar.*

class BankAccountActivity : FinanceBaseActivity() {

    var list = mutableListOf<BankCard>()


    lateinit var adapter: BankAccountAdapter


    override fun setResourceId(): Int {
        return R.layout.activity_bank_account
    }

    override fun preInitData() {
        super.preInitData()

        val data = getBankCarsFromLocal()

        if (data != null) {
            list.addAll(data)
        }

        adapter = BankAccountAdapter(this, list!!)

        lvBankAccount.adapter = adapter

        tvTitle.text = "账户"

    }

    override fun initViews() {

        tvAdd.typeface= Typeface.createFromAsset(assets,"icon_action.ttf")

        tvAdd.text=resources.getString(R.string.add)

        tvAdd.visibility= View.VISIBLE

        tvAdd.textSize=20f
    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            startActivity(Intent(this@BankAccountActivity,BankCardAddActivity::class.java))
        }
    }

    override fun initData() {
    }

}