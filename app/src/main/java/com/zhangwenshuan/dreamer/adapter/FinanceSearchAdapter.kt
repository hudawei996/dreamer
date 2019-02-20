package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.FinanceAddActivity
import com.zhangwenshuan.dreamer.bean.DayBill
import com.zhangwenshuan.dreamer.util.decimalFormat

class FinanceSearchAdapter(var context: Context, var list: MutableList<DayBill>) :

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var lastDate = ""

    var expense="支出:0.00"

    var income="收入:0.00"


    val TYPE_HEADER = 1

    val TYPE_NORMAL = 2

    var TYPE_FOOTER = 3

    private var beginTimeListener: View.OnClickListener? = null

    private var stopTimeListener: View.OnClickListener? = null

    private var beginTime = ""

    private var stopTime = ""

    private var footerText = "空空如也"


    override fun getItemCount(): Int = list.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {


        if (getItemViewType(position) == TYPE_HEADER) {
            holder as FinanceHeaderHolder

            holder.tvBegin.text = beginTime

            holder.tvStop.text = stopTime

            holder.tvExpense.text=expense

            holder.tvIncome.text=income

            holder.llBeginTime.setOnClickListener {
                beginTimeListener?.onClick(it)
            }

            holder.llStopTime.setOnClickListener {
                stopTimeListener?.onClick(it)
            }
            return
        }

        if (getItemViewType(position) == TYPE_FOOTER) {
            holder as FinanceFooterHolder
            lastDate = ""
            holder.tvText.text = footerText
            return
        }


        holder as FinanceSearchHolder

        val bill = list[position - 1]

        holder.tvDate.text = bill.date

        holder.tvIncome.text = """收入：${decimalFormat.format(bill.income)}"""

        holder.tvExpense.text = """支持：${decimalFormat.format(bill.expense)}"""

        holder.rvDetail.layoutManager = LinearLayoutManager(context)


        var adapter=FinanceDetailAdapter(context, bill.list!!)


        adapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                var intent= Intent(context,FinanceAddActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("finance", bill.list!![position])
                intent.putExtra("data", bundle)
                context.startActivity(intent)
            }
        })

        holder.rvDetail.adapter = adapter


    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        }

        if (position == list.size + 1) {
            return TYPE_FOOTER
        }

        return TYPE_NORMAL

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            return FinanceHeaderHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_finance_search_header,
                    parent,
                    false
                )
            )
        }

        if (viewType == TYPE_FOOTER) {
            return FinanceFooterHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_finance_search_footer,
                    parent,
                    false
                )
            )
        }


        return FinanceSearchHolder(LayoutInflater.from(context).inflate(R.layout.item_finance_search, null, false))
    }


    fun setBeginTime(beginTime: String) {
        this.beginTime = beginTime
    }

    fun setStopTime(stopTime: String) {
        this.stopTime = stopTime
    }

    fun setFooterText(text: String) {
        this.footerText = text
    }

    fun setBeginTimeListener(listener: View.OnClickListener) {
        beginTimeListener = listener
    }

    fun setStopTimeListener(listener: View.OnClickListener) {
        stopTimeListener = listener
    }

    fun setExpense(account:Double){
        expense="支出:${decimalFormat.format(account)}"
    }

    fun setIncome(account:Double){
        income="收入:${decimalFormat.format(account)}"
    }

}

class FinanceSearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvDate = itemView.findViewById<TextView>(R.id.tvSearchTime)
    val rlSearchTop = itemView.findViewById<RelativeLayout>(R.id.rlSearchTop)
    val tvIncome = itemView.findViewById<TextView>(R.id.tvSearchIncome)
    val tvExpense = itemView.findViewById<TextView>(R.id.tvSearchExpense)
    val rvDetail = itemView.findViewById<RecyclerView>(R.id.rvFinanceDetail)

}

class FinanceHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val llBeginTime = itemView.findViewById<LinearLayout>(R.id.llBeginTime)
    val llStopTime = itemView.findViewById<LinearLayout>(R.id.llStopTime)
    val tvBegin = itemView.findViewById<TextView>(R.id.tvSearchBeginTime)
    val tvStop = itemView.findViewById<TextView>(R.id.tvSearchStopTime)
    val tvExpense = itemView.findViewById<TextView>(R.id.tvExpense)
    val tvIncome = itemView.findViewById<TextView>(R.id.tvIncome)

}

class FinanceFooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvText = itemView.findViewById<TextView>(R.id.tvFinanceFooter)

}

