package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.decimalFormat
import com.zhangwenshuan.dreamer.bean.Finance

class FinanceTodayAdapter(val context: Context, val list: MutableList<Finance>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return FinanceTodayHolder(LayoutInflater.from(context).inflate(R.layout.item_finance_today, p0, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as FinanceTodayHolder

        val finance = list[position]

        holder.tvAccount.text = "￥${decimalFormat.format(finance.account)}元"



        if (finance.isExpense == 0) {
            holder.tvAccount.setTextColor(context.resources.getColor(R.color.finance_top_color))
        } else {
            holder.tvAccount.setTextColor(context.resources.getColor(R.color.finance_btn_add_start_color))
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }


        holder.tvName.text = list[position].type

        holder.tvTime.text = finance.date + " " + finance.time + " " + list[position].remark

    }


    override fun getItemCount(): Int = list.size


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    lateinit var listener: OnItemClickListener

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

class FinanceTodayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTime = itemView.findViewById<TextView>(R.id.tvFinanceTodayTime)
    val tvName = itemView.findViewById<TextView>(R.id.tvFinanceTodayName)
    val tvAccount = itemView.findViewById<TextView>(R.id.tvFinanceTodayAccount)
    val vLine = itemView.findViewById<View>(R.id.vBottomLine)

}

