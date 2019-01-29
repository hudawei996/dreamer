package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Item

class GeneralAdapter(var context: Context, var list: MutableList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return GeneralHolder(LayoutInflater.from(context).inflate(R.layout.item_general_adapter, parent, false))
    }

    override fun getItemCount(): Int = list.size

    val typeface = Typeface.createFromAsset(context.assets, "icon_action.ttf")

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as GeneralHolder

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


        holder.tvIcon.setTextColor(item.iconColor)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

        if (!item.subTitle.isEmpty()){
            holder.tvHint.text=item.subTitle
        }

        if (!item.value.isEmpty()){
            holder.tvValue.text=item.value
        }

        initView(holder,item.style)
    }

    lateinit var listener: OnItemClickListener


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private  fun initView(holder:GeneralHolder,style:GeneralStyle){
        when (style) {
            GeneralStyle.STYLE_NORMAL -> {
                holder.tvHint.visibility = View.GONE
                holder.tvValue.visibility = View.GONE


                holder.tvItem.gravity = Gravity.CENTER_VERTICAL
            }
            GeneralStyle.STYLE_HAVE_HINT -> {
                holder.tvHint.visibility = View.VISIBLE

                holder.tvValue.visibility = View.GONE

                holder.tvItem.gravity = Gravity.BOTTOM
            }
            GeneralStyle.STYLE_HAVE_VALUE -> {
                holder.tvHint.visibility = View.GONE

                holder.tvValue.visibility = View.VISIBLE

                holder.tvItem.gravity = Gravity.CENTER_VERTICAL
            }
            GeneralStyle.STYLE_HAVE_HINT_AND_VALUE -> {
                holder.tvHint.visibility = View.VISIBLE

                holder.tvValue.visibility = View.VISIBLE

                holder.tvItem.gravity = Gravity.BOTTOM
            }
        }
    }
}

class GeneralHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvIcon = itemView.findViewById<TextView>(R.id.tvIcon)
    val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
    val tvRight = itemView.findViewById<TextView>(R.id.tvRight)
    val tvHint = itemView.findViewById<TextView>(R.id.tvHint)
    val tvValue = itemView.findViewById<TextView>(R.id.tvValue)
    val vTop = itemView.findViewById<View>(R.id.vMarginTop)

}

enum class GeneralStyle {
    STYLE_NORMAL, STYLE_HAVE_HINT, STYLE_HAVE_VALUE, STYLE_HAVE_HINT_AND_VALUE
}


