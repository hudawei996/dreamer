package com.zhangwenshuan.dreamer.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.FinanceBudgetAdapter
import com.zhangwenshuan.dreamer.adapter.FinanceBudgetHeaderHolder
import com.zhangwenshuan.dreamer.bean.Budget
import com.zhangwenshuan.dreamer.bean.BudgetAdd
import com.zhangwenshuan.dreamer.bean.BudgetBean
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.GsonUtils
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance_budget_update.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class FinanceBudgetActivity : FinanceBaseActivity() {
    var list: MutableList<Budget> = mutableListOf()

    lateinit var adapter: FinanceBudgetAdapter

    override fun setResourceId(): Int {
        return R.layout.activity_finance_budget_update
    }



    override fun preInitData() {
        super.preInitData()

        var budget=BudgetBean()

        val bundle=intent.getBundleExtra("data")

        if (bundle!=null){
           budget=bundle.getSerializable("budget") as BudgetBean
        }


        var eat = Budget(R.mipmap.ic_finance_eat, "餐饮",budget.eat)
        list.add(eat)
        var transportation = Budget(R.mipmap.ic_finance_transportation, "交通",budget.transportation)
        list.add(transportation)
        var shopping = Budget(R.mipmap.ic_finance_shopping, "购物",budget.shopping)
        list.add(shopping)
        var amusement = Budget(R.mipmap.ic_finance_amusement, "娱乐",budget.amusement)
        list.add(amusement)
        var medical = Budget(R.mipmap.ic_finance_medical, "医教",budget.medical)
        list.add(medical)
        var home = Budget(R.mipmap.ic_finance_house, "家居",budget.house)
        list.add(home)
        var investment = Budget(R.mipmap.ic_finance_investment, "投资",budget.investment)
        list.add(investment)
        var social = Budget(R.mipmap.ic_finance_social, "人情",budget.social)
        list.add(social)
        var business = Budget(R.mipmap.ic_finance_business, "生意",budget.business)
        list.add(business)


        adapter = FinanceBudgetAdapter(this, list)

        adapter.total=budget.account
    }

    override fun initViews() {

        tvTitle.text = "预算"

        tvAdd.text = "完成"

        tvAdd.visibility = View.VISIBLE

        rvFinanceBudget.layoutManager = LinearLayoutManager(this)

        rvFinanceBudget.adapter = adapter


    }

    lateinit var holder: FinanceBudgetHeaderHolder


    override fun initListener() {


        tvAdd.setOnClickListener {

            rvFinanceBudget.isFocusableInTouchMode=true

            rvFinanceBudget.requestFocus()


            TimeUtils.closeInput(this)

            var total = adapter.getTotalAccount()

            if (total == 0.00) {
                toast("未设置预算")
                return@setOnClickListener
            }

            val budget = initBudgetData(total)

            val jsonData = GsonUtils.getGson().toJson(budget)


            val body = RequestBody.create(MediaType.parse("application/json"), jsonData)

            NetUtils.data(NetUtils.getApiInstance().saveBudget(body), Consumer {
                if (it.code == 200) {
                    EventBus.getDefault().post(BudgetAdd(it.data))
                    finish()
                }

                toast(it.message)
            })
        }
    }

    private fun initBudgetData(total: Double): BudgetBean {
        val budget = BudgetBean()

        budget.account = total

        for ((index, value) in list.withIndex()) {
            when (index) {
                0 -> budget.eat = value.account
                1 -> budget.transportation = value.account
                2 -> budget.shopping = value.account
                3 -> budget.amusement = value.account
                4 -> budget.medical = value.account
                5 -> budget.house = value.account
                6 -> budget.investment = value.account
                7 -> budget.social = value.account
                8 -> budget.business = value.account
            }
        }

        budget.userId = BaseApplication.userId
        budget.month = TimeUtils.curMonth()

        return budget
    }

    override fun initData() {
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}