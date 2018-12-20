package com.zhangwenshuan.dreamer.activity

import android.app.Activity
import android.content.Intent
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankSynopsisAdapter
import com.zhangwenshuan.dreamer.bean.BankCard
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance.*
import kotlinx.android.synthetic.main.activity_finance_synopsis.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FinanceSynopsisActivity : BaseActivity() {

    val showbankNumbers = 4

    var list = mutableListOf<BankCard>()

    var noLocalData = false

    lateinit var adapter: BankSynopsisAdapter

    var isMoreBank = false


    override fun setResourceId(): Int = R.layout.activity_finance_synopsis

    override fun preInitData() {
        EventBus.getDefault().register(this)

        var data = getBankCarsFromLocal()

        if (data == null) {
            noLocalData = true
        } else {

            if (data.size > 0 && (data[data.size - 1].name == "")) {
                data.removeAt(data.size - 1)
            }

            if (data.size > showbankNumbers) {
                isMoreBank = true
                list!!.addAll(data.subList(0, showbankNumbers))
            } else {
                isMoreBank = false
                list!!.addAll(data)
            }
        }


    }


    override fun initViews() {
        tvTitle.text = "财务总览"

        adapter = BankSynopsisAdapter(this, list!!)

        gvBank.adapter = adapter

        if (!noLocalData) {
            initAccountView(list!!)
        }

        initMoreBankView()

    }


    override fun initListener() {
        llMoreBank.setOnClickListener {
            if (!isMoreBank) {
                val intent = Intent(this@FinanceSynopsisActivity, BankCardAddActivity::class.java)
                startActivity(intent)
            } else {
                startActivity(Intent(this@FinanceSynopsisActivity,BankAccountActivity::class.java))
            }
        }

        llFianceSearch.setOnClickListener {
            val intent = Intent(this@FinanceSynopsisActivity, FinanceSearchActivity::class.java)
            startActivityForResult(intent, 1000)
        }
    }

    override fun initData() {
        if (noLocalData) {
            toGetBankCards()
        }


        toGetTotalAccount()
    }

    private fun toGetTotalAccount() {
        NetUtils.data(NetUtils.getApiInstance().getTotalFinance(BaseApplication.userId), Consumer {
            if (it.code == 200) {
                tvIncomeTotalAccount.text = decimalFormat.format(it.data.incomeAccount)
                tvExpenseTotalAccount.text = decimalFormat.format(it.data.expenseAccount)
            }
        })
    }


    private fun toGetBankCards() {
        NetUtils.data(
            NetUtils.getApiInstance().getBank(BaseApplication.userId),
            Consumer {
                logInfo(it.toString())


                if (it.code == 200) {

                    initAccountView(it.data)

                    if (it.data.size > showbankNumbers) {
                        isMoreBank = true
                        list!!.addAll(it.data.subList(0, showbankNumbers))
                    } else {
                        isMoreBank = false
                        list!!.addAll(
                            it.data
                        )
                    }

                    adapter.notifyDataSetChanged()

                    initMoreBankView()

                    if (it.data.isNotEmpty()) {
                        LocalDataUtils.setString(LocalDataUtils.BANK_CARD, GsonUtils.getGson().toJson(it.data))
                    }


                }

            }
        )
    }


    var account = 0.0

    private fun initAccountView(data: List<BankCard>) {
        for (value in data) {
            account += value.account
        }

        tvTotalAccount.text = decimalFormat.format(account)

    }


    private fun initMoreBankView() {
        if (!isMoreBank) {
            tvMoreBank.text = "添加账户"
        } else {
            tvMoreBank.text = "更多资产"
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun addBankCard(bank:BankCard){
        if (list.size < showbankNumbers) {
            list.add(bank!!)
            adapter.notifyDataSetChanged()
        } else {
            isMoreBank = true
            initMoreBankView()
        }

        account += bank!!.account

        tvTotalAccount.text = decimalFormat.format(account)

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}