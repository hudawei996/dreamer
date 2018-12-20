package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.BankCard
import com.zhangwenshuan.dreamer.bean.RightBean


/**
 * Created by zhangwenshuan on 2018/10/17.
 */
class DialogAdapter<T:RightBean>(var list: MutableList<T>, val context: Context) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: DialogHolder

        var view: View

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_bank_card_list, null)
            holder = DialogHolder(view)
            view.tag = holder

        } else {
            view = convertView
            holder = view.tag as DialogHolder
        }


        if(list[position] is BankCard){
            val card=list[position] as BankCard
            holder.tvName.text=card.name+card.id

        }else{
            holder.tvName.text = list[position]?.title

        }




        return view
    }


    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long = list.size.toLong()

    override fun getCount(): Int = list.size


}

class DialogHolder(itemView: View) {
    val tvName = itemView.findViewById<TextView>(R.id.tvBankListName)

}




