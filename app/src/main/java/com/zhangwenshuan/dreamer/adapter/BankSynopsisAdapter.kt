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

class BankSynopsisAdapter(var context: Context, var list: MutableList<Bank>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View

        var holder: BankSynopsisHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bank_list, parent, false)

            holder = BankSynopsisHolder()

            holder.tvAccount = view.findViewById(R.id.tvBankAccount)

            holder.tvName = view.findViewById(R.id.tvBankName)

            view.tag = holder

        } else {
            view = convertView

            holder = view.tag as BankSynopsisHolder
        }

        val bank = list[position]


        if (bank.type=="credit"){
            holder.tvAccount.text="-"+decimalFormat.format(list[position].debt)
        }else{
            holder.tvAccount.text = decimalFormat.format(list[position].account)

        }

        holder.tvName.text = list[position].name


        return view


    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size
}

class BankSynopsisHolder {
    lateinit var tvName: TextView
    lateinit var tvAccount: TextView
}