package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Logo
import com.zhangwenshuan.dreamer.bean.getImageRes

class LogoAdapter(val context: Context, val list: MutableList<Logo>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var holder:LogoHolder

        var view:View?=null

        if (convertView==null){
            view=LayoutInflater.from(context).inflate(R.layout.item_password_logo,parent,false)

            holder= LogoHolder(view)

            view.tag=holder
        }else{
            holder=convertView.tag as LogoHolder
            view=convertView
        }


        holder.tvName.text=list[position].name


        holder.ivLogo.setImageResource(getImageRes(list[position].type))


        return view!!

    }

    override fun getItem(position: Int): Any {
        return  list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}

class LogoHolder(itemView: View){
    val ivLogo=itemView.findViewById<ImageView>(R.id.ivLogo)
    val tvName=itemView.findViewById<TextView>(R.id.tvLogo)
}