package com.zhangwenshuan.dreamer.activity

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.CountDownListAdapter
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.adapter.OnItemSelectedListener
import com.zhangwenshuan.dreamer.bean.CountDown
import com.zhangwenshuan.dreamer.bean.TargetAdd
import com.zhangwenshuan.dreamer.bean.TargetFirst
import com.zhangwenshuan.dreamer.bean.TargetRemove
import com.zhangwenshuan.dreamer.custom.ItemTouchHelperCallback
import com.zhangwenshuan.dreamer.custom.RecyclerViewCallback
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.DBHelper
import com.zhangwenshuan.dreamer.util.getScreenPoint
import com.zhangwenshuan.dreamer.util.logInfo
import kotlinx.android.synthetic.main.activity_count_down.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CountDownListActivity : BaseActivity() {
    lateinit var reader: SQLiteDatabase

    lateinit var writer: SQLiteDatabase

    override fun setResourceId(): Int {
        return R.layout.activity_count_down
    }

    private var list = mutableListOf<CountDown>()

    private lateinit var adapter: CountDownListAdapter

    var querySql = "select * from dreamer where user_id=${BaseApplication.userId} order by oder asc"


    override fun preInitData() {
        EventBus.getDefault().register(this)
        list = mutableListOf()

        var helper = DBHelper(this)

        reader = helper.readableDatabase

        writer = helper.writableDatabase


        list.addAll(queryData(querySql))


        adapter = CountDownListAdapter(this, list)

        tvAdd.visibility = View.VISIBLE

        tvAdd.text = resources.getText(R.string.add)

        tvAdd.typeface = Typeface.createFromAsset(assets, resources.getString(R.string.typeface_path))

        tvAdd.textSize = 24f

        notifyDataChanged()
    }

    private fun queryData(sql: String): MutableList<CountDown> {
        var data = mutableListOf<CountDown>()

        var cursor = reader.rawQuery(sql, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    var target = cursor.getString(cursor.getColumnIndex("target"))
                    var beginTime = cursor.getString(cursor.getColumnIndex("begin_time"))
                    var endTime = cursor.getString(cursor.getColumnIndex("end_time"))
                    var createdTime = cursor.getString(cursor.getColumnIndex("created_time"))
                    var final = cursor.getInt(cursor.getColumnIndex("final"))
                    var order = cursor.getInt(cursor.getColumnIndex("oder"))
                    var id = cursor.getInt(cursor.getColumnIndex("id"))
                    var show = cursor.getInt(cursor.getColumnIndex("show"))
                    data.add(CountDown(id, target, createdTime, beginTime, endTime, order, final, show))

                } while (cursor.moveToNext())
            }

            cursor.close()

        }

        return data
    }

    override fun initViews() {
        tvSubtitle.text = "目标倒计时"
        tvSubtitle.visibility = View.VISIBLE
        tvTitle.visibility = View.GONE

        rvCountDown.layoutManager = LinearLayoutManager(this)

        rvCountDown.adapter = adapter


        val itemTouchHelperCallback = ItemTouchHelperCallback(object : RecyclerViewCallback {
            override fun onMove(fromPosition: Int, toPosition: Int) {
                adapter.onItemMove(fromPosition, toPosition)

                isMove = true


            }

            override fun onSwiped(position: Int) {
                toDelete(list[position].id)
                adapter.onItemSwipe(position)

            }
        }, true, true)

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        itemTouchHelper.attachToRecyclerView(rvCountDown)


        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                onChanged()
            }


            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }
        }


        )
    }

    private fun toDelete(id: Int) {
        writer.delete("dreamer", "id=$id", null)
    }


    private fun toUpdateOrder() {
        for (value in list) {
            var content = ContentValues()
            content.put("oder", value.order)
            writer.update("dreamer", content, " id=${value.id}", null)
        }
    }

    private fun notifyDataChanged() {
        if (list.size == 0) {
            llHint.visibility = View.VISIBLE
            rvCountDown.visibility = View.GONE

            EventBus.getDefault().post(TargetRemove())

        } else {
            llHint.visibility = View.GONE
            rvCountDown.visibility = View.VISIBLE
        }


    }

    lateinit var anchorView: View
    var touchX: Int? = 0
    var touchPosition: Int? = 0

    override fun initListener() {
        tvAdd.setOnClickListener {
            startActivity(Intent(this@CountDownListActivity, CountDownAddActivity::class.java))
        }


        adapter.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelectedListener(itemView: View, position: Int, x: Float) {

                anchorView = itemView

                touchX = x.toInt()

                touchPosition = position
            }

        })


        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                showMenuView(anchorView, touchX!!, touchPosition!!)
            }

        })


    }

    var isMove = false

    override fun onStop() {
        super.onStop()
        if (isMove) {

            if (list.size > 0) {
                EventBus.getDefault().post(TargetFirst(list[0]))
            }

            toUpdateOrder()
        }
    }

    override fun initData() {
        screenWidth = getScreenPoint().x


    }

    var popupWindow: PopupWindow? = null


    var screenWidth = 0

    var selectPosition = -1


    private fun showMenuView(anchorView: View, x: Int, position: Int) {
        var offsetX: Int

        var offsetY: Int


        if (popupWindow != null) {
            popupWindow!!.dismiss()
            if (selectPosition == position) {
                return
            }
        }

        selectPosition = position

        val anchorHeight = anchorView!!.height

        popupWindow = PopupWindow(this)

        popupWindow!!.setOnDismissListener {
            selectPosition = -1
        }

        popupWindow!!.contentView = LayoutInflater.from(this).inflate(R.layout.layout_count_down_menu, null, false)

        val view = popupWindow!!.contentView


        view.findViewById<TextView>(R.id.tvShow).setOnClickListener {

            list[selectPosition].show = 1

            EventBus.getDefault().post(TargetFirst(list[selectPosition]))

            var oldPosition = adapter.getShowPosition()



            if (oldPosition != -1) {
                list[oldPosition].show = 0
                var oldValue = ContentValues()
                oldValue.put("show", "0")
                writer.update("dreamer", oldValue, "id=${list[oldPosition].id}", null)

            }

            var newValue = ContentValues()
            newValue.put("show", "1")
            writer.update("dreamer", newValue, "id=${list[selectPosition].id}", null)


            adapter.notifyDataSetChanged()

            if (popupWindow != null) {
                popupWindow!!.dismiss()
            }
        }


        val tvFinish = view.findViewById<TextView>(R.id.tvFinish)

        if (list[selectPosition].final == 1) {
            tvFinish.text = "未完成"
        } else {
            tvFinish.text = "已完成"
        }

        tvFinish.setOnClickListener {
            var state = list[selectPosition].final


            var newValue = ContentValues()

            if (state == 1) {
                list[selectPosition].final = 0
                newValue.put("final", "0")
            } else {
                list[selectPosition].final = 1
                newValue.put("final", "1")

            }
            writer.update("dreamer", newValue, "id=${list[selectPosition].id}", null)


            adapter.notifyDataSetChanged()

            if (popupWindow != null) {
                popupWindow!!.dismiss()
            }

        }

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        val popupWidth = view.measuredWidth
        val popupHeight = view.measuredHeight


        if (x < popupWidth) {
            offsetX = popupWidth
        } else if (x > screenWidth - popupWidth) {
            offsetX = screenWidth - popupWidth * 2
        } else if(x>screenWidth/2){
            offsetX = x-popupWidth
        }else{
            offsetX = x
        }


        offsetY = -anchorHeight / 2 - popupHeight

        popupWindow!!.isOutsideTouchable = true

        popupWindow!!.isFocusable = true

        popupWindow!!.setBackgroundDrawable(ColorDrawable(0))

        popupWindow!!.showAsDropDown(anchorView, offsetX, offsetY, Gravity.TOP)


    }


    @Subscribe
    fun subscribe(add: TargetAdd) {
        list.clear()

        list.addAll(queryData(querySql))

        notifyDataChanged()

        adapter.notifyDataSetChanged()


        if (list.size == 1) {
            EventBus.getDefault().post(TargetFirst(list[0]))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

    }
}