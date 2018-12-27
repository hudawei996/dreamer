package com.zhangwenshuan.dreamer.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.FinanceDetailAdapter
import com.zhangwenshuan.dreamer.bean.Finance
import com.zhangwenshuan.dreamer.bean.Result
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import com.zhangwenshuan.dreamer.util.logInfo
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance_detail.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import java.text.DecimalFormat

const val TODAY_INCOME_DETAIL = 0
const val TODAY_EXPENSE_DETAIL = 1
const val MONTH_INCOME_DETAIL = 2
const val MONTH_EXPENSE_DETAIL = 3
const val DAY_DETAIL = 4
const val MONTH_DETAIL = 5
const val BANK_DETAIL = 6

var decimalFormat = DecimalFormat("#,##0.00")

class FinanceDetailActivity : FinanceBaseActivity() {

    var state: Int = TODAY_INCOME_DETAIL

    lateinit var list: MutableList<Finance>

    lateinit var adapter: FinanceDetailAdapter

    override fun setResourceId(): Int {
        return R.layout.activity_finance_detail
    }

    override fun preInitData() {
        super.preInitData()

        state = intent.getIntExtra("state", TODAY_INCOME_DETAIL)

        list = mutableListOf()

        adapter = FinanceDetailAdapter(this, list)
    }

    override fun initViews() {
        rvFinanceDetail.layoutManager = LinearLayoutManager(this)

        rvFinanceDetail.adapter = adapter

        initTitleBar()
    }

    private fun initTitleBar() {
        if (state == TODAY_INCOME_DETAIL) {
            tvTitle.text = "本日收入明细"
            return
        }
        if (state == MONTH_INCOME_DETAIL) {
            tvTitle.text = "本月收入明细"
            return
        }
        if (state == TODAY_EXPENSE_DETAIL) {
            tvTitle.text = "本日支出明细"
            return
        }

        if (state == MONTH_EXPENSE_DETAIL) {
            tvTitle.text = "本月支出明细"
            return
        }

        if (state == DAY_DETAIL) {
            tvTitle.text = "本日收支明细"
            return
        }

        if (state == MONTH_DETAIL) {
            tvTitle.text = "本月收支明细"
            return
        }

        if (state == BANK_DETAIL) {
            tvTitle.text = intent.getStringExtra("title")
            return
        }
    }

    override fun initListener() {
    }

    override fun initData() {
        toGetFinance()
    }

    private fun toGetFinance() {
        var time = TimeUtils.curDay()
        if (state == TODAY_INCOME_DETAIL) {
            getFinance(time, 0)
        } else if (state == TODAY_EXPENSE_DETAIL) {
            getFinance(time, 1)
        } else if (state == MONTH_INCOME_DETAIL) {
            time = TimeUtils.curMonth()
            getFinance(time, 0)
        } else if (state == MONTH_EXPENSE_DETAIL) {
            time = TimeUtils.curMonth()
            getFinance(time, 1)
        } else if (state == DAY_DETAIL) {
            time = TimeUtils.curDay()
            getFinance(time, -1)
        } else if (state == MONTH_DETAIL) {
            time = TimeUtils.curMonth()
            getFinance(time, -1)
        } else if (state == BANK_DETAIL) {
            getBankFinanceDetail()
        }
    }

    private fun getBankFinanceDetail() {
        NetUtils.data(NetUtils.getApiInstance().getFinanceByBankId(intent.getIntExtra("bankId", 0)), Consumer {
            getDataCallback(it)
        })
    }

    private fun getFinance(time: String, isExpense: Int = 0) {
        NetUtils.data(NetUtils.getApiInstance().getFinanceByTime(BaseApplication.userId, time, isExpense),
            Consumer {
                logInfo(it.toString())

                getDataCallback(it)

            })
    }

    private fun getDataCallback(it: Result<List<Finance>>) {
        if (it.code == 200) {
            list.addAll(it.data)
            adapter.notifyDataSetChanged()
        }

        if (list.size == 0) {
            rvFinanceDetail.visibility = View.GONE
            tvNoFinance.visibility = View.VISIBLE
        } else {

            if (state == BANK_DETAIL) {
                return
            }

            var total = 0.0

            for (value in list) {
                if (value.isExpense == 0) {
                    total += value.account
                } else {
                    total -= value.account
                }
            }

            tvAdd.text = "￥" + decimalFormat.format(total)
            tvAdd.visibility = View.VISIBLE
        }
    }
}