package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.CountDown
import com.zhangwenshuan.dreamer.util.TimeUtils
import com.zhangwenshuan.dreamer.util.logInfo
import java.util.*

class CountDownListAdapter(var context: Context, var list: MutableList<CountDown>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return CountDownListHolder(LayoutInflater.from(context).inflate(R.layout.item_count_down, parent, false))
    }

    override fun getItemCount(): Int = list.size

    private var showPosition=-1


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as CountDownListHolder

        val item = list[position]

        holder.tvName.text = item.name

        holder.tvTime.text = item.beginTime + " 至 " + item.endTime

        holder.tvCreatedTime.text = item.createTime

        var endTime = TimeUtils.toTimestamp(item.endTime)
        var beginTime = TimeUtils.toTimestamp(item.beginTime)
        var curTime = System.currentTimeMillis()

        if (beginTime > curTime) {
            holder.tvCount.text = "未开始"
        } else {
            var date = TimeUtils.timeDifference(System.currentTimeMillis(), endTime)[0]

            if (date >= 0) {
                holder.tvCount.text = date.toString()
            } else {
                holder.tvCount.text = date.toString()
                if (item.final == 1) {

                    holder.ivFinish.visibility = View.VISIBLE
                    holder.ivOutTime.visibility = View.GONE
                } else {
                    holder.ivFinish.visibility = View.GONE
                    holder.ivOutTime.visibility = View.VISIBLE
                }
            }
        }

        if (item.show==1){
            showPosition=position
        }

        if (item.final == 1) {
            holder.tvCount.text = "0"
            holder.ivFinish.visibility = View.VISIBLE
        } else {
            holder.ivFinish.visibility = View.GONE
        }



        holder.itemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action == MotionEvent.ACTION_DOWN) {
                    listener.onItemSelectedListener(v!!, position, event!!.x)

                }

                return false
            }
        })


        holder.itemView.setOnClickListener {
            clickListener.onItemClick(position)
        }


        item.order = position

    }

    lateinit var listener: OnItemSelectedListener
    lateinit var clickListener: OnItemClickListener


    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    fun setOnItemClickListener(listener:OnItemClickListener){
        clickListener=listener
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {

        val fromOrder = list[fromPosition].order

        val toOrder = list[toPosition].order

        list[fromPosition].order = toOrder

        list[toPosition].order = fromOrder



        Collections.swap(list, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    fun getShowPosition():Int{
        return showPosition
    }

    fun onItemSwipe(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}

class CountDownListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName = itemView.findViewById<TextView>(R.id.tvName)
    val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
    val tvCount = itemView.findViewById<TextView>(R.id.tvDownCount)
    val tvCreatedTime = itemView.findViewById<TextView>(R.id.tvCreatedTime)
    val line = itemView.findViewById<View>(R.id.vLine)
    val ivFinish = itemView.findViewById<ImageView>(R.id.ivFinish)
    val ivOutTime = itemView.findViewById<ImageView>(R.id.ivOutTime)

}

interface OnItemSelectedListener {
    fun onItemSelectedListener(itemView: View, position: Int, x: Float)
}

