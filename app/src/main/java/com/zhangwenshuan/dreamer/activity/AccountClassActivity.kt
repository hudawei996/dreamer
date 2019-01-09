package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankListAdapter
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AccountClassActivity : FinanceBaseActivity() {

    val list = mutableListOf<Setting>()

    lateinit var adapter: BankListAdapter

    override fun setResourceId(): Int {
        return R.layout.activity_setting
    }

    override fun preInitData() {
        super.preInitData()

        EventBus.getDefault().register(this)

        list.add(Setting(resources.getString(R.string.cash), "现金", showRight = true, showTopLine = true))
        list.add(Setting(resources.getString(R.string.credit), "信用卡", showRight = true))
        list.add(Setting(resources.getString(R.string.bank), "银行卡", showRight = true))
        list.add(Setting(resources.getString(R.string.mobile), "移动支付", showRight = true))

        adapter = BankListAdapter(this, list)

        rvSetting.adapter = adapter

        rvSetting.layoutManager = LinearLayoutManager(this)


    }

    override fun initViews() {

        tvTitle.text = "添加账户"

    }

    override fun initListener() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        startActivity(Intent(this@AccountClassActivity, AccountCashActivity::class.java))
                    }
                    1 -> {
                        startActivity(Intent(this@AccountClassActivity, AccountCreditActivity::class.java))
                    }
                    2 -> {
                        startActivity(Intent(this@AccountClassActivity, AccountBankActivity::class.java))
                    }
                    3 -> {
                        startActivity(Intent(this@AccountClassActivity, AccountMobileActivity::class.java))
                    }

                }
            }
        })
    }

    override fun initData() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribe(cashAdd: CashAdd) {
        finish()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribe(cashAdd: BankAdd1) {
        finish()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribe(cashAdd: CreditAdd) {
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribe(cashAdd: MobileAdd) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}