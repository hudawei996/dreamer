package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.BankCard
import java.text.DecimalFormat


class BankCardAdapter : PagerAdapter {

    var context: Context

    var list: MutableList<BankCard>


    var bankFont: Typeface

    var decimalFormat = DecimalFormat("#,##0.00")


    constructor(context: Context, list: MutableList<BankCard>) : super() {
        this.context = context

        this.list = list

        bankFont = Typeface.createFromAsset(context.assets, "bank1.ttf")
    }


    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int = list?.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        var view: View


        if (position < list.size - 1) {


            view = LayoutInflater.from(context).inflate(R.layout.item_bank_card, null)

            val tvAccount = view.findViewById<TextView>(R.id.tvBalance)

            val tvName = view.findViewById<TextView>(R.id.tvBank)

            val tvNumber = view.findViewById<TextView>(R.id.tvBankNumber)

            val tvBankId = view.findViewById<TextView>(R.id.tvBankId)



            tvName!!.typeface = bankFont

            tvName!!.text = list[position].name

            tvAccount!!.text = "￥ " + decimalFormat.format(list[position].account)

            tvNumber!!.text = list[position].number

            tvBankId.text=list[position].id.toString()

            view.setOnClickListener {
                listener?.onItemClick(position)

            }


        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_bank_card_add, null)


            view.setOnClickListener {
                listener?.onItemClick(position)
            }

        }



        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        `object` as View

        container.removeView(`object`)

    }

    var listener: OnItemClickListener? = null

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}
