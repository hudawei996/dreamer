package com.zhangwenshuan.dreamer.adapter

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Password
import com.zhangwenshuan.dreamer.bean.getImageRes

class PasswordListAdapter(val context: Context, val list: MutableList<Password>) : BaseAdapter() {
    var curPosition=-1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var holder:PasswordHolder

        var view:View

        if (convertView==null){
            view=LayoutInflater.from(context).inflate(R.layout.item_password_list,parent,false)

            holder=PasswordHolder(view)

            view.tag=holder
        }else{
            holder=convertView.tag as PasswordHolder
            view=convertView
        }

        if (curPosition==position){
            holder.ivSee.setImageResource(R.mipmap.ic_visible)
            holder.etPassword.inputType=InputType.TYPE_TEXT_VARIATION_PASSWORD
        }else{
            holder.ivSee.setImageResource(R.mipmap.ic_gone)
            holder.etPassword.inputType=InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT

        }

        holder.tvName.text=list[position].name
        holder.tvUsername.text=list[position].username
        holder.etPassword.text=list[position].password

        holder.ivPassword.setImageResource(getImageRes(list[position].type))

        holder.ivSee.setOnClickListener {
            if (curPosition==position){
                curPosition=-1
            }else{
                curPosition=position
            }

            notifyDataSetChanged()
        }


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

class PasswordHolder(itemView: View){
    val ivPassword=itemView.findViewById<ImageView>(R.id.ivPassword)
    val ivSee=itemView.findViewById<ImageView>(R.id.ivSee)
    val tvName=itemView.findViewById<TextView>(R.id.tvPasswordName)
    val tvUsername=itemView.findViewById<TextView>(R.id.tvUsername)
    val etPassword=itemView.findViewById<TextView>(R.id.etPassword)
}