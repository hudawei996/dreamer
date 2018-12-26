package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.decimalFormat
import com.zhangwenshuan.dreamer.bean.Budget

class FinanceBudgetDetailAdapter(val context: Context, val list: MutableList<Budget>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return FinanceBudgetDetailHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_finance_budget_detail,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as FinanceBudgetDetailHolder

        var budget = list[position]

        holder.ivIndicator.setImageResource(budget.icon)

        holder.tvName.text = budget.name

        holder.tvAccount.text = "预算 ${decimalFormat.format(budget.account)}"

        val balance = budget.account - budget.balance

        if (balance >= 0) {
            holder.tvBalanceHint.text = "剩余"
            holder.tvBalanceHint.setTextColor(context.resources.getColor(R.color.hint))

        } else {
            holder.tvBalanceHint.setTextColor(context.resources.getColor(R.color.finance_base_color))
            holder.tvBalanceHint.text = "超支"
            -balance
        }

        holder.tvBalance.text = decimalFormat.format(balance)

        val ratio = (balance * 100 / budget.account).toInt()

        holder.pb.progress = 100 - ratio


    }


    override fun getItemCount(): Int = list.size


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}

class FinanceBudgetDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivIndicator = itemView.findViewById<ImageView>(R.id.ivIndicator)
    val tvName = itemView.findViewById<TextView>(R.id.tvBudgetDetailItem)
    val tvAccount = itemView.findViewById<TextView>(R.id.tvBudgetDetailAccount)
    val tvBalance = itemView.findViewById<TextView>(R.id.tvBudgetDetailBalance)
    val tvBalanceHint = itemView.findViewById<TextView>(R.id.tvBudgetDetailBalanceHint)
    val pb = itemView.findViewById<ProgressBar>(R.id.pbBudgetDetail)

}
