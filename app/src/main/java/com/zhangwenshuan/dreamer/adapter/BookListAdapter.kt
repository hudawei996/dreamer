package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Book

class BookListAdapter(var context: Context, var list: MutableList<Book>) : BaseAdapter() {

    var bookNameListener: OnItemClickListener? = null
    var endTimeListener: OnItemClickListener? = null


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View

        var holder: BookListHolder



        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_time_line, parent, false)

            holder = BookListHolder()

            holder.tvBegin = view.findViewById(R.id.tvStartTime)

            holder.tvEnd = view.findViewById(R.id.tvEndTime)

            holder.tvName = view.findViewById(R.id.tvContent)

            holder.bottomOval = view.findViewById(R.id.vBottomOval)
            holder.vLine = view.findViewById(R.id.vLine)
            holder.oval = view.findViewById(R.id.vOval)

            view.tag = holder

        } else {
            view = convertView

            holder = view.tag as BookListHolder
        }

        holder.tvName.setOnClickListener {
            bookNameListener?.onItemClick(position)
        }



        holder.tvName.text = """《${list[position].name}》"""

        holder.tvBegin.text = """始 ${list[position].begin}"""

        val end = list[position].end
        if (end == "在读") {
            holder.tvEnd.setTextColor(context.resources.getColor(R.color.colorPrimary))
            holder.vLine.background = context.resources.getDrawable(R.drawable.full_line_vertical)

            holder.tvName.setTextColor(context.resources.getColor(R.color.colorBase))

            holder.tvEnd.text = end


            holder.tvEnd.setOnClickListener {
                endTimeListener?.onItemClick(position)
            }

        } else {
            holder.tvEnd.text = """至 $end"""
            holder.tvEnd.setTextColor(context.resources.getColor(R.color.colorBase))

            holder.vLine.background = context.resources.getDrawable(R.drawable.full_line_vertical_finish)


            holder.tvName.setTextColor(context.resources.getColor(R.color.colorPrimary))

            if (position == 0) {
                holder.oval.background = context.resources.getDrawable(R.drawable.oval_finish)
            } else {
                holder.oval.background = context.resources.getDrawable(R.drawable.oval)
            }
        }



        if (position != 0) {
            holder.oval.visibility = View.GONE
        }

        return view


    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    fun setBookNameClickListener(onItemClickListener: OnItemClickListener) {
        bookNameListener = onItemClickListener
    }

    fun setEndTimeClickListener(onItemClickListener: OnItemClickListener) {
        endTimeListener = onItemClickListener
    }
}


class BookListHolder {
    lateinit var tvName: TextView
    lateinit var tvBegin: TextView
    lateinit var tvEnd: TextView

    lateinit var oval: View
    lateinit var bottomOval: View
    lateinit var vLine: View
}