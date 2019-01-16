package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.*
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

class MainFragment : BaseFragment() {
    override fun getLayoutResource(): Int {
        return R.layout.activity_finance
    }


    var monthIncome: Double = 0.0

    var monthExpense: Double = 0.0

    val list: MutableList<Finance> = mutableListOf()

    lateinit var financeAdapter: FinanceTodayAdapter

    var position = 0


    override fun beforeViewCreated(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity!!.window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }

        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        val statusHeight =
            resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

        rlTop.setPadding(0, statusHeight, 0, 0)

    }


    override fun preInitData() {

        EventBus.getDefault().register(this)
    }

    override fun initViews() {

        val financeFont = Typeface.createFromAsset(activity!!.assets, "finance.ttf")

        tvFinance.text = resources.getString(R.string.finance)

        tvFinance.typeface = financeFont

        financeAdapter = FinanceTodayAdapter(activity!!, list)

        rvTodayFinance.adapter = financeAdapter

        rvTodayFinance.layoutManager = LinearLayoutManager(activity)


        financeAdapter.setItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val finance = list[position]
                val intent = Intent(activity, FinanceAddActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("finance", finance)
                intent.putExtra("data", bundle)
                startActivity(intent)
                this@MainFragment.position = position
            }
        })
    }


    override fun initListener() {
        tvAddFinance.setOnClickListener {
            val intent = Intent(activity, FinanceAddActivity::class.java)
            startActivity(intent)
        }

        tvFinance.setOnClickListener {
            val intent = Intent(activity, FinanceSynopsisActivity::class.java)

            startActivity(intent)
        }

        tvFinanceBudget.setOnClickListener {
            if (budgetBean != null) {
                val intent = Intent(activity, FinanceBudgetDetailActivity::class.java)

                val bundle = Bundle()

                bundle.putSerializable("budget", budgetBean)

                bundle.putDouble("expense", monthExpense)

                intent.putExtra("data", bundle)

                startActivity(intent)

            } else {
                val intent = Intent(activity, FinanceBudgetActivity::class.java)

                startActivity(intent)
            }


        }

        tvFinanceMonthBudgetHint.setOnClickListener {
            tvFinanceBudget.performClick()
        }

        tvTodayMore.setOnClickListener {
            val intent = Intent(activity, FinanceSearchActivity::class.java)

            startActivity(intent)
        }
    }


    override fun initData() {

        getFinance()


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

    private fun getFinance() {
        NetUtils.data(
            NetUtils.getApiInstance().getFinanceByTime(BaseApplication.userId, TimeUtils.curMonth(), -1),
            Consumer {
                if (it.code == 200) {
                    list.addAll(it.data)
                    financeAdapter.notifyDataSetChanged()
                }

                initTotalView()

            })
    }

    var todayExpense = 0.0

    var todayIncome = 0.0


    private fun initTotalView() {
        monthIncome = 0.0

        monthExpense = 0.0

        todayExpense = 0.0

        todayIncome = 0.0

        var today = TimeUtils.curDay()

        for (value in list) {
            if (value.isExpense == 0) {
                monthIncome += value.account

                if (value.date == today) {
                    todayIncome += value.account
                }


            } else {
                monthExpense += value.account

                if (value.date == today) {
                    todayExpense += value.account
                }
            }


        }


        tvFinanceMonthIncome.text = decimalFormat.format(monthIncome)

        tvFinanceMonthExpense.text = decimalFormat.format(monthExpense)


        tvTodayExpense.text = "今日支出:${decimalFormat.format(todayExpense)}元"

        tvTodayIncome.text = "收入:${decimalFormat.format(todayIncome)}元"

        notifyBudgetViewChange()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(event: FinanceDelete) {
        list.remove(event.finance)

        financeAdapter.notifyDataSetChanged()

        initTotalView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(event: FinanceAdd) {
        list.add(0, event.finance)

        financeAdapter.notifyDataSetChanged()

        initTotalView()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(budget: BudgetAdd) {
        budgetBean = budget.budgetBean
        notifyBudgetViewChange()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(update: FinanceUpdate) {
        list[position] = update.finance

        financeAdapter.notifyItemChanged(position)

        initTotalView()
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}