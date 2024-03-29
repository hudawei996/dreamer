package com.zhangwenshuan.dreamer.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.style.IconMarginSpan
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.FinanceSearchAdapter
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance_search.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import java.util.*


class FinanceSearchActivity : FinanceBaseActivity() {

    var list = mutableListOf<DayBill>()

    lateinit var adapter: FinanceSearchAdapter

    lateinit var beginDate: String

    lateinit var stopDate: String

    var isBeginDate = true

    lateinit var startCalendar: Calendar

    lateinit var stopCalendar: Calendar


    override fun setResourceId(): Int {
        return R.layout.activity_finance_search
    }

    override fun preInitData() {
        super.preInitData()

        EventBus.getDefault().register(this)

        adapter = FinanceSearchAdapter(this, list)


        stopCalendar = Calendar.getInstance()


        startCalendar = Calendar.getInstance()

        startCalendar.set(Calendar.YEAR, 2018)
        startCalendar.set(Calendar.MONTH, 0)
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)

    }

    override fun initViews() {
        tvTitle.text = "账单搜索"

        tvAdd.text = "搜索"

        tvAdd.visibility = View.VISIBLE

        rvFinanceSearch.layoutManager = LinearLayoutManager(this)!!

        adapter = FinanceSearchAdapter(this, list)

        rvFinanceSearch.adapter = adapter


        stopDate = TimeUtils.curDay()

        beginDate = TimeUtils.curMonthFirstDay()

        adapter.setBeginTime(beginDate)

        adapter.setStopTime(stopDate)

        adapter.notifyDataSetChanged()

    }

    override fun initListener() {
        adapter.setBeginTimeListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                isBeginDate = true
                showTimePickerView()

            }

        })

        adapter.setStopTimeListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                isBeginDate = false
                showTimePickerView()
            }

        })

        tvAdd.setOnClickListener {
            toSearchFinance(beginDate, stopDate)
        }
    }

    override fun initData() {
        toSearchFinance(beginDate, stopDate)
    }

    private fun toSearchFinance(beginDate: String, stopDate: String) {
        if (beginDate > stopDate) {
            toast("起始日期不能大于终止日期")
            return
        }


        NetUtils.data(
            NetUtils.getApiInstance().getFinanceBySearch(BaseApplication.userId, beginDate, stopDate),
            Consumer {
                if (it.code == 200) {

                    list.clear()


                    val data = toDayBill(it.data)

                    list.addAll(data!!)

                    if (list.size == 0) adapter.setFooterText("---空空如也---") else adapter.setFooterText("---已经见底了哦---")


                    adapter.notifyDataSetChanged()

                    rvFinanceSearch.scrollToPosition(0)
                }
            })
    }

    private fun showTimePickerView() {
        val arrays = arrayOf(true, true, true, false, false, false)

        val pvTime = TimePickerBuilder(this@FinanceSearchActivity, object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                if (date != null) {
                    if (isBeginDate) {
                        beginDate = TimeUtils.getDay(date)
                        adapter.setBeginTime(beginDate)
                        adapter.notifyDataSetChanged()
                    } else {
                        stopDate = TimeUtils.getDay(date)
                        adapter.setStopTime(stopDate)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
            .setRangDate(startCalendar, stopCalendar)
            .setDate(stopCalendar)
            .setType(arrays.toBooleanArray())
            .setSubmitColor(resources.getColor(R.color.finance_base_color))//确定按钮文字颜色
            .setCancelColor(resources.getColor(R.color.finance_base_color))//取消按钮文字颜色
            .setTextColorCenter(resources.getColor(R.color.finance_base_color))
            .build()

        pvTime.show()
    }


    private fun toDayBill(list: List<Finance>): MutableList<DayBill> {

        var dayBills = mutableListOf<DayBill>()

        var dayBill: DayBill? = null

        var expense = 0.0
        var income = 0.0


        if (list.isNotEmpty()) {

            var lastData = ""

            for (value in list) {

                if (value.isExpense == 1) {
                    expense += value.account
                } else {
                    income += value.account
                }

                if (lastData == "" || value.date != lastData) {
                    lastData = value.date
                    dayBill = DayBill()
                    dayBill.date = value.date
                    dayBills.add(dayBill)
                }


                dayBill!!.list!!.add(value)

                if (value.isExpense == 1) {
                    dayBill.expense += value.account
                } else {
                    dayBill.income += value.account
                }
            }

            dayBills.sortByDescending { it.date }

            adapter.setExpense(expense)

            adapter.setIncome(income)

            return dayBills
        }

        return mutableListOf()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(update: FinanceUpdate) {
        toSearchFinance(beginDate, stopDate)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subscribeEvent(event: FinanceDelete) {
        toSearchFinance(beginDate, stopDate)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}