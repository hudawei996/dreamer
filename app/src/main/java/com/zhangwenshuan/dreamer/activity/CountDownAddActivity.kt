package com.zhangwenshuan.dreamer.activity

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.CountDown
import com.zhangwenshuan.dreamer.bean.TargetAdd
import com.zhangwenshuan.dreamer.bean.TargetFirst
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.DBHelper
import com.zhangwenshuan.dreamer.util.TimeUtils
import kotlinx.android.synthetic.main.activity_count_down_add.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import java.sql.SQLData
import java.util.*

class CountDownAddActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_count_down_add
    }


    lateinit var sqlWriter: SQLiteDatabase

    override fun preInitData() {
        var helper = DBHelper(this)

        sqlWriter = helper.writableDatabase
    }

    override fun initViews() {
        tvSubtitle.text = "添加目标"

        tvSubtitle.visibility = View.VISIBLE

        tvTitle.visibility = View.GONE

        var curDate = TimeUtils.curDay()

        tvBeginTime.text = curDate
        tvEndTime.text = curDate

    }

    override fun initListener() {
        tvBeginTime.setOnClickListener {
            showDateView(tvBeginTime)
        }


        tvEndTime.setOnClickListener {
            showDateView(tvEndTime)
        }

        tvSubmit.setOnClickListener {
            toSave()
        }
    }

    private fun toSave() {
        var target = etTarget.text.toString()

        if (target.isEmpty()) {
            toast("想实现什么目标呢？")
            return
        }

        var isShow = if (cb.isChecked) 1 else 0


        var value = ContentValues()
        value.put("begin_time", tvBeginTime.text.toString())
        value.put("end_time", tvEndTime.text.toString())
        value.put("target", etTarget.text.toString())
        value.put("created_time", TimeUtils.curDay())
        value.put("user_id", BaseApplication.userId)


        var result = sqlWriter.insert("dreamer", null, value)


        if (result > 0) {
            toast("保存成功")
            EventBus.getDefault().post(TargetAdd())
            finish()
        } else {
            toast("保存失败")
        }


    }


    private fun showDateView(textView: TextView) {
        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(
            this,
            DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    textView.text = "$year-${month + 1}-$dayOfMonth"
                    textView.setTextColor(resources.getColor(R.color.colorBlack))
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )



        dialog.show()

    }

    override fun initData() {
    }
}