package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.decimalFormat
import com.zhangwenshuan.dreamer.bean.Bank
import com.zhangwenshuan.dreamer.bean.BankCard

class BankAccountAdapter(var context: Context, var list: MutableList<Bank>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View

        var holder: BankAccountHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_account_list, parent, false)

            holder = BankAccountHolder()

            holder.tvAccount = view.findViewById(R.id.tvBankAccount)

            holder.tvName = view.findViewById(R.id.tvBankName)
            holder.tvAmount = view.findViewById(R.id.tvAmount)
            holder.tvDebt = view.findViewById(R.id.tvDebt)

            view.tag = holder

        } else {
            view = convertView

            holder = view.tag as BankAccountHolder
        }


        val bank = list[position]

        if (bank.type == "credit") {
            holder.tvAmount.visibility = View.VISIBLE
            holder.tvDebt.visibility = View.VISIBLE

            holder.tvAmount.text ="总资产 "+ decimalFormat.format(bank.amount)

            holder.tvDebt.text = "-" + decimalFormat.format(bank.debt)

            holder.tvAccount.text = decimalFormat.format(bank.amount - bank.debt)
        } else {
            holder.tvAmount.visibility = View.GONE
            holder.tvDebt.visibility = View.GONE

            holder.tvAccount.text = decimalFormat.format(list[position].account)
        }


        if (bank.number!=null&&!bank.number!!.isEmpty()){

            holder.tvName.text = "${bank.name}(${bank.number})"
        }else{
            holder.tvName.text = "${bank.name}"
        }



        return view


    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size
}

class BankAccountHolder {
    lateinit var tvName: TextView
    lateinit var tvAccount: TextView
    lateinit var tvDebt: TextView
    lateinit var tvAmount: TextView
}