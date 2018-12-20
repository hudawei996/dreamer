package com.zhangwenshuan.dreamer.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Adapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankCardAdapter
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.custom.XAxisCustom
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance.*
import kotlinx.android.synthetic.main.activity_finance_synopsis.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class FinanceActivity : BaseActivity() {


    override fun setResourceId(): Int = R.layout.activity_finance

    var todayIncome: Double = 0.0

    var monthIncome: Double = 0.0

    var monthExpense: Double = 0.0

    var todayExpense: Double = 0.0

    override fun preInitData() {
        EventBus.getDefault().register(this)

        val calendar = Calendar.getInstance()

        val format = SimpleDateFormat("MM-dd")


        weekX.add(format.format(calendar.time))

        for (it in 0..5) {
            calendar.add(Calendar.DATE, -1)
            weekX.add(format.format(calendar.time))
        }

        weekX.reverse()

        logInfo(weekX.toString())

    }

    override fun initViews() {
        val iconFont = Typeface.createFromAsset(assets, "expense_income.ttf")

        val accountFont = Typeface.createFromAsset(assets, "date_month.ttf")

        val financeFont = Typeface.createFromAsset(assets, "finance.ttf")


        tvExpense.text = resources.getString(R.string.expense)
        tvExpense.typeface = iconFont

        tvIncome.text = resources.getString(R.string.income)
        tvIncome.typeface = iconFont

        tvFinance.text = resources.getString(R.string.finance)
        tvFinance.typeface = financeFont


        tvIncomeHint.text = resources.getString(R.string.income_account)
        tvIncomeHint.typeface = accountFont

        tvExpenseHint.text = resources.getString(R.string.expense_account)
        tvExpenseHint.typeface = accountFont

        tvDay.text = resources.getString(R.string.day_account)
        tvDay.typeface = accountFont

        tvMonth.text = resources.getString(R.string.month_account)
        tvMonth.typeface = accountFont

        initWeekChart()

        initBankCard()

    }

    var bankCards = mutableListOf<BankCard>()

    lateinit var bankAdapter: BankCardAdapter


    private fun initBankCard() {


        bankAdapter = BankCardAdapter(this, bankCards)

        vpCard.adapter = bankAdapter
    }

    val weekX = mutableListOf<String>()

    val weekY = mutableListOf<Entry>()

    val expenseY = mutableListOf<Entry>()


    lateinit var incomeDataSet: LineDataSet

    lateinit var expensesDataSet: LineDataSet

    lateinit var lineData: LineData

    private fun initWeekChart() {

        lineData = LineData()

        lineChart.data = lineData

        lineChart.setTouchEnabled(false)


        val customXAis = XAxisCustom(weekX)

        val xAxis = lineChart.xAxis

        xAxis.granularity = 1f

        xAxis.valueFormatter = customXAis

        xAxis.setDrawGridLines(false)


        xAxis.position = XAxis.XAxisPosition.BOTTOM

        lineChart.axisRight.isEnabled = false

        lineChart.axisLeft.isEnabled = false

        lineChart.setNoDataText("没有数据哦...")

        lineChart.isDragEnabled = false

        lineChart.axisRight.setDrawZeroLine(false)

        lineChart.description.text = ""

        lineChart.invalidate()


    }

    override fun initListener() {
        tvExpenseHint.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceAddActivity::class.java)
            intent.putExtra("isExpense", 1)
            startActivity(intent)
        }

        tvFinance.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceSynopsisActivity::class.java)

            startActivity(intent)
        }

        tvIncomeHint.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceAddActivity::class.java)
            intent.putExtra("isExpense", 0)
            startActivity(intent)
        }

        bankAdapter.setOnClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@FinanceActivity, BankCardAddActivity::class.java)

                bankPosition = position

                if (position < bankCards.size - 1) {
                    val bundle = Bundle()

                    bundle.putSerializable("bank", bankCards[position])

                    intent.putExtra("data", bundle)
                    this@FinanceActivity.startActivity(intent)
                } else {
                    this@FinanceActivity.startActivity(intent)

                }

            }
        })

        tvDayIncome.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceDetailActivity::class.java)
            intent.putExtra("state", TODAY_INCOME_DETAIL)
            startActivity(intent)
        }

        tvDayExpense.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceDetailActivity::class.java)
            intent.putExtra("state", TODAY_EXPENSE_DETAIL)
            startActivity(intent)
        }

        tvMonthIncome.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceDetailActivity::class.java)
            intent.putExtra("state", MONTH_INCOME_DETAIL)
            startActivity(intent)
        }

        tvMonthExpense.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceDetailActivity::class.java)
            intent.putExtra("state", MONTH_EXPENSE_DETAIL)
            startActivity(intent)
        }

        tvMonth.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceDetailActivity::class.java)
            intent.putExtra("state", MONTH_DETAIL)
            startActivity(intent)
        }


        tvDay.setOnClickListener {
            val intent = Intent(this@FinanceActivity, FinanceDetailActivity::class.java)
            intent.putExtra("state", DAY_DETAIL)
            startActivity(intent)
        }

    }


    var bankPosition = 0

    override fun initData() {
        toGetFinance()
        toGetBank()
    }

    @SuppressLint("CheckResult")
    private fun toGetFinance() {
        NetUtils.data(
            NetUtils.getApiInstance().getFinance(BaseApplication.userId),
            Consumer {
                logInfo(it.toString())

                val curDate = TimeUtils.curDay()

                val account = it.data

                monthIncome = account.monthIncome

                monthExpense = account.monthExpense

                tvMonthExpense.text = decimalFormat.format(account.monthExpense)

                tvMonthIncome.text = decimalFormat.format(account.monthIncome)

                val incomes = account.latelyWeekIncomeAccount

                if (incomes != null) {
                    for (index in 0..6) {
                        weekY.add(Entry(index.toFloat(), 0f))
                    }

                    for (value in incomes) {
                        if (curDate == value.date) {
                            todayIncome = value.account
                            tvDayIncome.text = decimalFormat.format(value.account)
                        }

                        for ((index, date) in weekX.withIndex()) {

                            if (date == value.date.substring(5, value.date.length)) {

                                weekY[index] = Entry(index.toFloat(), value.account.toFloat())
                            }
                        }

                    }
                }

                val expenses = account.latelyWeekExpenseAccount

                if (expenses != null) {
                    for (index in 0..6) {
                        expenseY.add(Entry(index.toFloat(), 0f))
                    }

                    for (value in expenses) {
                        if (curDate == value.date) {
                            todayExpense = value.account
                            tvDayExpense.text = decimalFormat.format(value.account)
                        }

                        for ((index, date) in weekX.withIndex()) {

                            if (date == value.date.substring(5, value.date.length)) {
                                expenseY[index] = Entry(index.toFloat(), value.account.toFloat())
                            }
                        }

                    }
                }

                incomeDataSet = LineDataSet(weekY, "收入")

                incomeDataSet.color = resources.getColor(R.color.colorOrange)

                expensesDataSet = LineDataSet(expenseY, "支出")

                expensesDataSet.color = resources.getColor(R.color.colorPrimary)


                lineData.addDataSet(incomeDataSet)

                lineData.addDataSet(expensesDataSet)

                lineData.notifyDataChanged()

                lineChart.notifyDataSetChanged()

                lineChart.invalidate()

            })
    }

    fun toGetBank() {
        NetUtils.data(
            NetUtils.getApiInstance().getBank(BaseApplication.userId),
            Consumer {
                logInfo(it.toString())


                if (it.code == 200) {
                    bankCards.addAll(it.data)

                    bankCards.add(BankCard("", 0.toDouble(), "", 1))

                    bankAdapter.notifyDataSetChanged()


                    if (it.data.size > 0) {
                        LocalDataUtils.setString(LocalDataUtils.BANK_CARD, GsonUtils.getGson().toJson(it.data))
                    }


                }

            }
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {

                bankCards.removeAt(bankPosition)

                bankAdapter.notifyDataSetChanged()

            } else if (requestCode == 1001) {

                val bank = data?.getBundleExtra("data")!!.getSerializable("bank") as BankCard?


                if (bankCards != null) {


                    bankCards.add(bankCards.size - 1, bank!!)

                    bankAdapter.notifyDataSetChanged()
                }

            }

            LocalDataUtils.setString(LocalDataUtils.BANK_CARD, GsonUtils.getGson().toJson(bankCards))

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(event: EventBean) {
        when (event.flag) {
            EVENT_SAVE_INCOME -> {
                todayIncome += event.message!!.toDouble()
                monthIncome += event.message!!.toDouble()

                tvDayIncome.text = decimalFormat.format(todayIncome)
                tvMonthIncome.text = decimalFormat.format(monthIncome)
            }
            EVENT_SAVE_EXPENSE -> {
                todayExpense += event.message!!.toDouble()
                monthExpense += event.message!!.toDouble()

                tvDayExpense.text = decimalFormat.format(todayExpense)
                tvMonthExpense.text = decimalFormat.format(monthExpense)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun addBankCard(bank: BankCard) {
        if (bankCards != null) {

            bankCards.add(bankCards.size - 1, bank!!)

            bankAdapter.notifyDataSetChanged()
        }

        LocalDataUtils.setString(LocalDataUtils.BANK_CARD, GsonUtils.getGson().toJson(bankCards))

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun removeBankCard(bank: RemoveBank) {
        bankCards.removeAt(bankPosition)

        bankAdapter.notifyDataSetChanged()

        LocalDataUtils.setString(LocalDataUtils.BANK_CARD, GsonUtils.getGson().toJson(bankCards))

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}