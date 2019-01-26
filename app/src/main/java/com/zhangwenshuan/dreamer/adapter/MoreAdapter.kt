package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Item

class MoreAdapter(var context: Context, var list: MutableList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return MoreHolder(LayoutInflater.from(context).inflate(R.layout.item_more, parent, false))
    }

    override fun getItemCount(): Int = list.size

    val typeface = Typeface.createFromAsset(context.assets, "icon_action.ttf")

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as MoreHolder

        val item = list[position]

        if (item.showRight) {
            holder.tvRight.visibility = View.VISIBLE
        } else {
            holder.tvRight.visibility = View.GONE
        }

        if (item.showTop) {
            holder.vTop.visibility = View.VISIBLE
        } else {
            holder.vTop.visibility = View.GONE
        }

        if (item.icon.isEmpty()) {
            holder.tvIcon.visibility = View.GONE
        } else {
            holder.tvIcon.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

        holder.tvRight.typeface = typeface
        holder.tvIcon.typeface = typeface


        holder.tvItem.text = item.name

        holder.tvRight.text = context.resources.getString(R.string.to_right)
        holder.tvIcon.text = item.icon


        if (position == 0) {
            holder.tvIcon.setTextColor(context.resources.getColor(R.color.finance_base_color))
        } else if (position == 1) {
            holder.tvIcon.setTextColor(context.resources.getColor(R.color.chart_color_4))

        } else if (position == 2) {
            holder.tvIcon.setTextColor(context.resources.getColor(R.color.chart_color_1))

        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    lateinit var listener: OnItemClickListener


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

class MoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvIcon = itemView.findViewById<TextView>(R.id.tvIcon)
    val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
    val tvRight = itemView.findViewById<TextView>(R.id.tvRight)
    val vTop = itemView.findViewById<View>(R.id.vMarginTop)

}

