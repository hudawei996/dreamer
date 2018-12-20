package com.zhangwenshuan.dreamer.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.DialogAdapter
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.RightBean
import com.zhangwenshuan.dreamer.util.getScreenPoint
import kotlinx.android.synthetic.main.layout_right_dialog.*


/**
 * Created by MuQuan on 2018/11/2 0002.
 * Features：
 */
class RightDialog<T:RightBean>( context: Context,var list: MutableList<T>) : Dialog(context, R.style.RightDialogStyle) {



    lateinit var adapter: DialogAdapter<T>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_right_dialog)


        adapter = DialogAdapter(list, context)

        lvRightDialog.adapter = adapter



        lvRightDialog.onItemClickListener =object:AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                listener.onItemClick(position)
            }
        }


        window.setGravity(Gravity.RIGHT)

        val params = window.attributes
        params.width = getScreenPoint().x / 3
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params


    }

    lateinit var listener:OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener=listener
    }


}

