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
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.decimalFormat
import com.zhangwenshuan.dreamer.bean.Budget

class FinanceBudgetAdapter(val context: Context, val list: MutableList<Budget>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var headerHolder: FinanceBudgetHeaderHolder

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == HEADER) {
            return FinanceBudgetHeaderHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_finance_budget_header,
                    p0,
                    false
                )
            )
        }
        return FinanceBudgetHolder(LayoutInflater.from(context).inflate(R.layout.item_finance_budget, p0, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is FinanceBudgetHeaderHolder) {
            this.headerHolder = holder
            headerHolder.etTotal.text = Editable.Factory.getInstance().newEditable(decimalFormat.format(total))

            return
        }


        if (holder is FinanceBudgetHolder) {

            var budget = list[position - 1]

            holder.ivIndicator.setImageResource(budget.icon)

            holder.tvName.text = budget.name


            if (holder.etAccount.tag != null && holder.etAccount.tag is TextWatcher) {
                holder.etAccount.removeTextChangedListener(holder.etAccount.tag as TextWatcher)
            }

            if (budget.account != 0.0) {
                holder.etAccount.text =
                        Editable.Factory.getInstance().newEditable(decimalFormat.format(budget.account))

            } else {
                holder.etAccount.text = Editable.Factory.getInstance().newEditable("")
            }





            holder.etAccount.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    var s = holder.etAccount.text.toString().replace(",", "")

                    if (!s.isEmpty()) {
                        var account = s.toDouble()

                        budget.account = account

                        toCalculateTotalAccount()

                        holder.etAccount.text =
                                Editable.Factory.getInstance().newEditable(decimalFormat.format(account))
                    }
                }


            }


        }
    }

    var total = 0.0

    private fun toCalculateTotalAccount() {
        total = 0.0

        for (value in list) {
            total += value.account

        }

        if (headerHolder != null) {
            headerHolder.etTotal.text = Editable.Factory.getInstance().newEditable(decimalFormat.format(total))
        }


    }


    val HEADER = 1
    val NORMAL = 0

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return HEADER
        } else {
            return NORMAL
        }
    }

    override fun getItemCount(): Int = list.size + 1


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getTotalAccount(): Double {
        return total
    }


}

class FinanceBudgetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivIndicator = itemView.findViewById<ImageView>(R.id.ivIndicator)
    val tvName = itemView.findViewById<TextView>(R.id.tvBudgetName)
    val etAccount = itemView.findViewById<EditText>(R.id.etBudgetAccount)

}

class FinanceBudgetHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val etTotal = itemView.findViewById<EditText>(R.id.etFinanceBudgetTotal)
}