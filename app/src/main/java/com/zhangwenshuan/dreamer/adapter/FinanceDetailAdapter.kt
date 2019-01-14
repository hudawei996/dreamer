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

            holder.tvAccount.setTextColor(context.resources.getColor(R.color.finance_base_color))
        } else {
            holder.tvAccount.setTextColor(context.resources.getColor(R.color.finance_top_color))

        }
        holder.tvAccount.text = "￥ ${finance.account}"

        holder.tvRemark.text = finance.time + " " + finance.remark

        holder.tvTitle.text = finance.item + " " + finance.type


        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    lateinit var listener: OnItemClickListener


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

class FinanceDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle = itemView.findViewById<TextView>(R.id.tvFinanceName)
    val tvAccount = itemView.findViewById<TextView>(R.id.tvFinanceAccount)
    val tvRemark = itemView.findViewById<TextView>(R.id.tvFinanceRemark)

}

