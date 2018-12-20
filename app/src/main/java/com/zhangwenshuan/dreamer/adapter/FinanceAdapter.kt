package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Finance

class FinanceAdapter(val context: Context, val list: MutableList<Finance>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return FinanceHolder(LayoutInflater.from(context).inflate(R.layout.item_finance,p0,false))
    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as FinanceHolder

        holder.tvDate.text=list[position].time

        holder.tvExpense.text=list[position].account.toString()

        holder.tvIncome.text=list[position].account.toString()
    }



    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}

class FinanceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvDate=itemView.findViewById<TextView>(R.id.tvDate)
    val tvExpense=itemView.findViewById<TextView>(R.id.tvExpenseAccount)
    val tvIncome=itemView.findViewById<TextView>(R.id.tvIncomeAccount)

}