package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.FinanceBudgetDetailAdapter
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import com.zhangwenshuan.dreamer.util.decimalFormat
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance_budget_detail.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FinanceBudgetDetailActivity : FinanceBaseActivity() {

    var list: MutableList<Budget> = mutableListOf()

    lateinit var adapter: FinanceBudgetDetailAdapter

    override fun setResourceId(): Int {
        return R.layout.activity_finance_budget_detail
    }

    lateinit var budget: BudgetBean

    var expense: Double = 0.0

    override fun preInitData() {
        super.preInitData()

        EventBus.getDefault().register(this)

        budget = BudgetBean()


        val bundle = intent.getBundleExtra("data")

        if (bundle != null) {
            budget = bundle.getSerializable("budget") as BudgetBean

            expense = bundle.getDouble("expense")

        }


        initListData()


        adapter = FinanceBudgetDetailAdapter(this, list)

        rvFinanceBudgetDetail.layoutManager = LinearLayoutManager(this)

        rvFinanceBudgetDetail.adapter = adapter

        initTotalBudgetView()

    }

    private fun initTotalBudgetView() {
        tvBudgetTotal.text = "总预算 " + decimalFormat.format(budget.account)

        var month = budget.month!!.split("-")

        tvSubtitle.text = month[0] + "年" + month[1] + "月预算"


        val balance = budget.account - expense

        if (balance > 0) {
            tvBudgetBalanceHint.text = "预算余额"
            tvBudgetBalance.text = decimalFormat.format(balance)
        } else {
            tvBudgetBalanceHint.text = "预算超支"
            tvBudgetBalanceHint.setTextColor(resources.getColor(R.color.colorWhite))
            tvBudgetBalance.text = decimalFormat.format(-balance)
        }
    }

    private fun initListData() {
        var eat = Budget(R.mipmap.ic_finance_eat, "餐饮", budget.eat)
        list.add(eat)
        var transportation = Budget(R.mipmap.ic_finance_transportation, "交通", budget.transportation)
        list.add(transportation)
        var shopping = Budget(R.mipmap.ic_finance_shopping, "购物", budget.shopping)
        list.add(shopping)
        var amusement = Budget(R.mipmap.ic_finance_amusement, "娱乐", budget.amusement)
        list.add(amusement)
        var medical = Budget(R.mipmap.ic_finance_medical, "医教", budget.medical)
        list.add(medical)
        var home = Budget(R.mipmap.ic_finance_house, "家居", budget.house)
        list.add(home)
        var investment = Budget(R.mipmap.ic_finance_investment, "投资", budget.investment)
        list.add(investment)
        var social = Budget(R.mipmap.ic_finance_social, "人情", budget.social)
        list.add(social)
        var business = Budget(R.mipmap.ic_finance_business, "生意", budget.business)
        list.add(business)
    }

    override fun initViews() {
        tvTitle.visibility = View.GONE
        tvSubtitle.visibility = View.VISIBLE

        tvAdd.visibility = View.VISIBLE

        tvAdd.text = resources.getString(R.string.edit)
        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            val intent = Intent(this@FinanceBudgetDetailActivity, FinanceBudgetActivity::class.java)

            val bundle = Bundle()

            bundle.putSerializable("budget", budget)

            intent.putExtra("data", bundle)

            startActivity(intent)

        }
    }


    lateinit var budgetList: List<BudgetDetail>

    override fun initData() {
        NetUtils.data(
            NetUtils.getApiInstance().getBudgetDetail(BaseApplication.userId, TimeUtils.curMonth(),1),
            Consumer {
                if (it.code == 200) {

                    budgetList = it.data

                    for (value in list) {
                        for (data in budgetList) {
                            if (data.item == value.name) {
                                value.balance = data.account
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
            })
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun subscribe(data: BudgetAdd) {
        budget = data.budgetBean

        list.clear()

        initListData()

        for (value in list) {
            for (data in budgetList) {
                if (data.item == value.name) {
                    value.balance = data.account
                }
            }
        }

        runOnUiThread {
            adapter.notifyDataSetChanged()

            initTotalBudgetView()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}