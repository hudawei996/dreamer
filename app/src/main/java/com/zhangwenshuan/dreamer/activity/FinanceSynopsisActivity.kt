package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Color
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankSynopsisAdapter
import com.zhangwenshuan.dreamer.bean.BankCard
import com.zhangwenshuan.dreamer.custom.MyValueFormatter
import com.zhangwenshuan.dreamer.custom.XAxisCustom
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance.*
import kotlinx.android.synthetic.main.activity_finance_synopsis.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.collections.forEachWithIndex

class FinanceSynopsisActivity : FinanceBaseActivity() {

    val showbankNumbers = 4

    var list = mutableListOf<BankCard>()


    lateinit var adapter: BankSynopsisAdapter

    var isMoreBank = false

    val description = Description()

    lateinit var intColors: MutableList<Int>


    override fun setResourceId(): Int = R.layout.activity_finance_synopsis


    override fun preInitData() {
        super.preInitData()

        EventBus.getDefault().register(this)

        description.text = ""


        val strColors = resources.getStringArray(R.array.chart_color)


        intColors = mutableListOf<Int>()

        strColors.toList().forEachWithIndex { i, pieEntry ->
            intColors.add(Color.parseColor(strColors[i]))
        }
    }

    private fun toGetFinanceItemDetail() {
        NetUtils.data(
            NetUtils.getApiInstance().getBudgetDetail(BaseApplication.userId, TimeUtils.curMonth(), 1),
            Consumer {
                if (it.code == 200) {

                    it.data.sortedByDescending { it.account }

                    var account = it.data.sumBy { it.account.toInt() }

                    var total = 0.00

                    for ((index, value) in it.data.withIndex()) {

                        if (index > 6) {
                            total += value.account
                            continue
                        }

                        var result = value.account * 100 / account

                        val pieEntry = PieEntry(result.toFloat(), value.item)
                        pieEntries.add(pieEntry)
                    }

                    if (total != 0.00) {
                        val pieEntry = PieEntry(total.toFloat(), "其他")
                        pieEntries.add(pieEntry)
                    }

                    initPieChart()


                }
            })
    }


    override fun initViews() {
        tvTitle.text = "财务总览"

        adapter = BankSynopsisAdapter(this, list!!)

        gvBank.adapter = adapter

        initMoreBankView()

        pcMonthItem.setNoDataText("网络太遥远，我正在努力的奔跑")

        barChart.setNoDataText("看看楼下的兄弟在干嘛")
    }

    var pieEntries = mutableListOf<PieEntry>()

    private fun initPieChart() {


        val pieDataSet = PieDataSet(pieEntries, "")

        pieDataSet.colors = intColors


        val pieData = PieData(pieDataSet)

        pieData.setValueFormatter(PercentFormatter())

        pieData.setValueTextColor(resources.getColor(R.color.colorWhite))

        pcMonthItem.data = pieData


        pcMonthItem.description = description


        pcMonthItem.holeRadius = 0f

        pcMonthItem.setTransparentCircleAlpha(0)

        pcMonthItem.legend.isEnabled = true

        pcMonthItem.setDrawEntryLabels(false)

        pcMonthItem.legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT

        pcMonthItem.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER

        pcMonthItem.animateXY(2000, 2000)

        pcMonthItem.invalidate()
    }


    override fun initListener() {
        llMoreBank.setOnClickListener {
            if (!isMoreBank) {
                val intent = Intent(this@FinanceSynopsisActivity, BankCardAddActivity::class.java)
                startActivity(intent)
            } else {
                startActivity(Intent(this@FinanceSynopsisActivity, BankAccountActivity::class.java))
            }
        }

        llFianceSearch.setOnClickListener {
            val intent = Intent(this@FinanceSynopsisActivity, FinanceSearchActivity::class.java)
            startActivityForResult(intent, 1000)
        }
    }

    override fun initData() {

        toGetBankCards()

        toGetTotalAccount()

        toGetFinanceItemDetail()
    }

    private fun toGetTotalAccount() {
        NetUtils.data(NetUtils.getApiInstance().getTotalFinance(BaseApplication.userId), Consumer {
            if (it.code == 200) {
                tvIncomeTotalAccount.text = decimalFormat.format(it.data.incomeAccount)
                tvExpenseTotalAccount.text = decimalFormat.format(it.data.expenseAccount)
            }
        })
    }

    val rawBankCard = mutableListOf<BankCard>()

    private fun toGetBankCards() {
        NetUtils.data(
            NetUtils.getApiInstance().getBank(BaseApplication.userId),
            Consumer {
                logInfo(it.toString())


                if (it.code == 200) {

                    rawBankCard.addAll(it.data)

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

                    initBarChartView()

                    LocalDataUtils.setString(LocalDataUtils.BANK_CARD, GsonUtils.getGson().toJson(it.data))
                }

            }
        )
    }

    var barEntries = mutableListOf<BarEntry>()

    var bankXAxis = mutableListOf<String>()

    private fun initBarChartView() {
        rawBankCard.forEachWithIndex { i, bankCard ->
            val barEntry = BarEntry(i.toFloat(), bankCard.account.toFloat())
            barEntries.add(barEntry)
            bankXAxis.add(bankCard.name)
        }

        val dataSet = BarDataSet(barEntries, "")


        dataSet.colors = intColors

        val barData = BarData(dataSet)


        barChart.data = barData


        barChart.setTouchEnabled(false)

        barChart.axisLeft.setDrawGridLines(false)

        barChart.axisLeft.setDrawZeroLine(true)

        barChart.axisRight.isEnabled = false

        barChart.xAxis.isEnabled = true

        barChart.xAxis.valueFormatter = XAxisCustom(bankXAxis)

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        barChart.xAxis.setDrawGridLines(false)

        barChart.legend.isEnabled=false

        barChart.axisLeft.setDrawLabels(false)

        barChart.axisLeft.setDrawAxisLine(false)

    //    barChart.xAxis.setLabelCount(bankXAxis.size,true)


        if (barEntries.size>6){
            barChart.xAxis.textSize=6f
        }

        barChart.description = description

        barChart.animateXY(2000,3000)


        barChart.invalidate()


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
    fun addBankCard(bank: BankCard) {
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