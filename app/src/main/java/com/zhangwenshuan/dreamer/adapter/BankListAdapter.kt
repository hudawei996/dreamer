package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Setting

class BankListAdapter(val context: Context, val list: MutableList<Setting>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val typeface = Typeface.createFromAsset(context.assets, "icon_action.ttf")

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return BankListHolder(LayoutInflater.from(context).inflate(R.layout.item_bank_card_list, p0, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BankListHolder

        val setting = list[position]

        if (setting.showBottomLine) {
            holder.vBottomLine.visibility = View.VISIBLE
        } else {
            holder.vBottomLine.visibility = View.GONE
        }

        if (setting.showTopLine) {
            holder.vMarginLine.visibility = View.VISIBLE
        } else {
            holder.vMarginLine.visibility = View.GONE
        }

        if (setting.showRight) {
            holder.tvSettingRight.visibility = View.VISIBLE
        } else {
            holder.tvSettingRight.visibility = View.GONE
        }

        holder.tvSettingIndicator.typeface = typeface

        holder.tvSettingIndicator.text = setting.indicator

        holder.tvSettingRight.text = context.resources.getString(R.string.to_right)

        holder.tvSettingRight.typeface = typeface

        holder.tvSettingName.text = setting.name


        if (position == 0) {
            holder.tvSettingIndicator.setTextColor(context.resources.getColor(R.color.chart_color_2))
        } else if(position==1){
            holder.tvSettingIndicator.setTextColor(context.resources.getColor(R.color.chart_color_3))
        }else if (position==2){

            holder.tvSettingIndicator.setTextColor(context.resources.getColor(R.color.chart_color_5))
        }else if (position==3){
            holder.tvSettingIndicator.setTextColor(context.resources.getColor(R.color.chart_color_6))

        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

}

class BankListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvSettingIndicator = itemView.findViewById<TextView>(R.id.tvSettingIndicator)
    val tvSettingName = itemView.findViewById<TextView>(R.id.tvSettingName)
    val tvSettingRight = itemView.findViewById<TextView>(R.id.tvSettingRight)
    val vMarginLine = itemView.findViewById<View>(R.id.vMarginLine)
    val vBottomLine = itemView.findViewById<View>(R.id.vBottomLine)

}