package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import android.widget.Adapter
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.FinanceTodayAdapter
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class FinanceActivity : BaseActivity() {


    override fun setResourceId(): Int = R.layout.activity_finance


    var monthIncome: Double = 0.0

    var monthExpense: Double = 0.0

    val list: MutableList<Finance> = mutableListOf()

    lateinit var financeAdapter: FinanceTodayAdapter

    var position = 0


    override fun preInitData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        val statusHeight =
            resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

        rlTop.setPadding(0, statusHeight, 0, 0)

        EventBus.getDefault().register(this)
    }

    override fun initViews() {

        val financeFont = Typeface.createFromAsset(assets, "finance.ttf")

        tvFinance.text = resources.getString(R.string.finance)

        tvFinance.typeface = financeFont

        financeAdapter = FinanceTodayAdapter(this, list)

        rvTodayFinance.adapter = financeAdapter

        rvTodayFinance.layoutManager = LinearLayoutManager(this)


        financeAdapter.setItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val finance = list[position]
                val intent = Intent(this@FinanceActivity, FinanceAddActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("finance", finance)
                intent.putExtra("data", bundle)
                startActivity(intent)
                this@FinanceActivity.position = position
            }
        })
    }


    override fun initListener() {
        tvAddFinance.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceAddActivity::class.java)
            startActivity(intent)
        }

        tvFinance.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceSynopsisActivity::class.java)

            startActivity(intent)
        }

        tvFinanceBudget.setOnClickListener {
            if (budgetBean != null) {
                val intent = Intent(this@FinanceActivity, FinanceBudgetDetailActivity::class.java)

                val bundle = Bundle()

                bundle.putSerializable("budget", budgetBean)

                bundle.putDouble("expense", monthExpense)

                intent.putExtra("data", bundle)

                startActivity(intent)

            } else {
                val intent = Intent(this@FinanceActivity, FinanceBudgetActivity::class.java)

                startActivity(intent)
            }


        }

        tvFinanceMonthBudgetHint.setOnClickListener {
            tvFinanceBudget.performClick()
        }
    }


    override fun initData() {

        getTodayFinance()


        getBudget()

    }

    var budgetBean: BudgetBean? = null

    private fun getBudget() {
        NetUtils.data(NetUtils.getApiInstance().getBudget(BaseApplication.userId, TimeUtils.curMonth()), Consumer {
            if (it.code == 200) {
                budgetBean = it.data

                notifyBudgetViewChange()
            }
        })
    }

    private fun notifyBudgetViewChange() {
        if (monthExpense > budgetBean!!.account) {
            tvFinanceBudget.text = decimalFormat.format(monthExpense - budgetBean!!.account)
            tvFinanceBudget.setTextColor(resources.getColor(R.color.finance_base_color))
            tvFinanceMonthBudgetHint.text = "预算超支"
        } else {
            tvFinanceBudget.text = decimalFormat.format(budgetBean!!.account - monthExpense)
            tvFinanceBudget.setTextColor(resources.getColor(R.color.colorWhite))
            tvFinanceMonthBudgetHint.text = "预算余额"
        }
    }

    private fun getTodayFinance() {
        NetUtils.data(NetUtils.getApiInstance().getFinanceByTime(BaseApplication.userId, TimeUtils.curMonth(), -1),
            Consumer {
                if (it.code == 200) {
                    list.addAll(it.data)
                    financeAdapter.notifyDataSetChanged()
                }

                initTotalView()

            })
    }

    private fun initTotalView() {
        monthIncome=0.0
        monthExpense=0.0

        for (value in list) {
            if (value.isExpense == 0) {
                monthIncome += value.account
            } else {
                monthExpense += value.account
            }

            tvFinanceMonthIncome.text = decimalFormat.format(monthIncome)

            tvFinanceMonthExpense.text = decimalFormat.format(monthExpense)

        }

        notifyBudgetViewChange()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(event: FinanceDelete) {
        if (event.finance.isExpense == 1) {
            monthExpense -= event.finance.account
            tvFinanceMonthExpense.text = decimalFormat.format(monthExpense)
        } else {
            monthIncome -= event.finance.account
            tvFinanceMonthExpense.text = decimalFormat.format(monthIncome)
        }

        list.remove(event.finance)

        financeAdapter.notifyDataSetChanged()

        notifyBudgetViewChange()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(event: FinanceAdd) {
        if (event.finance.isExpense == 1) {
            monthExpense += event.finance.account
            tvFinanceMonthExpense.text = decimalFormat.format(monthExpense)
        } else {
            monthIncome += event.finance.account
            tvFinanceMonthIncome.text = decimalFormat.format(monthIncome)
        }


        list.add(0, event.finance)

        financeAdapter.notifyDataSetChanged()

        notifyBudgetViewChange()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(budget: BudgetAdd) {
        budgetBean = budget.budgetBean
        notifyBudgetViewChange()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(budget: FinanceUpdate) {
        list[position] = budget.finance

        financeAdapter.notifyItemChanged(position)

        initTotalView()
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}