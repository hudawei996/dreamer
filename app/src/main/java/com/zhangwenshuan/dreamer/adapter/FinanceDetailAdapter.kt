package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Finance

class FinanceDetailAdapter(var context: Context, var list: MutableList<Finance>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return FinanceDetailHolder(LayoutInflater.from(context).inflate(R.layout.item_finance_detail, null, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as FinanceDetailHolder

        val finance = list[position]


        if (finance.isExpense == 1) {

            holder.tvAccount.text = "￥ -${finance.account}"
        } else {
            holder.tvAccount.text = "￥ ${finance.account}"

        }
        holder.tvRemark.text = finance.remark
        holder.tvTime.text = finance.date + " " + finance.time
        holder.tvTitle.text = finance.bankName + "-" + finance.type
    }
}

class FinanceDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle = itemView.findViewById<TextView>(R.id.tvFinanceName)
    val tvAccount = itemView.findViewById<TextView>(R.id.tvFinanceAccount)
    val tvRemark = itemView.findViewById<TextView>(R.id.tvFinanceRemark)
    val tvTime = itemView.findViewById<TextView>(R.id.tvFinanceTime)

}
