package com.zhangwenshuan.dreamer.fragment

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.activity.CountDownListActivity
import com.zhangwenshuan.dreamer.bean.CountDown
import com.zhangwenshuan.dreamer.bean.TargetFirst
import com.zhangwenshuan.dreamer.bean.TargetRemove
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.DBHelper
import com.zhangwenshuan.dreamer.util.TimeUtils
import com.zhangwenshuan.dreamer.util.logInfo
import kotlinx.android.synthetic.main.fragment_count_down_time.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CountDownTimeFragment : BaseFragment() {
    override fun getLayoutResource(): Int {
        return R.layout.fragment_count_down_time
    }


    lateinit var reader: SQLiteDatabase

    override fun setStatusBarHeight(height: Int) {
        if (vTop != null) {
            var layoutParams = vTop.layoutParams

            var oldHeight = layoutParams.height

            logInfo(oldHeight.toString())


            layoutParams.height = height

            vTop.layoutParams = layoutParams
        }


    }

    var showCountDown: CountDown? = null
    var firstCountDown: CountDown? = null

    override fun preInitData() {
        EventBus.getDefault().register(this)

        reader = DBHelper(activity!!).readableDatabase

        queryData(reader, querySql)

    }

    private fun initCountDown() {
        var time = TimeUtils.toTimestamp(showCountDown!!.endTime)

        var dayList = TimeUtils.timeDifference(System.currentTimeMillis(), time)

        tvDayCount.text = dayList[0].toString()

        tvHourCount.text = dayList[1].toString()

        tvTarget.text = showCountDown!!.name

        tvTime.text = "${showCountDown!!.beginTime} 至 ${showCountDown!!.endTime}"
    }

    var querySql = "select * from dreamer where user_id=${BaseApplication.userId} order by oder asc"


    private fun queryData(db: SQLiteDatabase, sql: String) {

        var cursor = db.rawQuery(sql, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    var show = cursor.getInt(cursor.getColumnIndex("show"))

                    if (show == 1) {
                        var target = cursor.getString(cursor.getColumnIndex("target"))
                        var beginTime = cursor.getString(cursor.getColumnIndex("begin_time"))
                        var endTime = cursor.getString(cursor.getColumnIndex("end_time"))
                        var createdTime = cursor.getString(cursor.getColumnIndex("created_time"))
                        var order = cursor.getInt(cursor.getColumnIndex("oder"))
                        var id = cursor.getInt(cursor.getColumnIndex("id"))
                        var final = cursor.getInt(cursor.getColumnIndex("final"))
                        showCountDown = CountDown(id, target, createdTime, beginTime, endTime, order, final)
                    } else {
                        var order = cursor.getInt(cursor.getColumnIndex("oder"))

                        if (order == 0) {
                            var final = cursor.getInt(cursor.getColumnIndex("final"))
                            var target = cursor.getString(cursor.getColumnIndex("target"))
                            var beginTime = cursor.getString(cursor.getColumnIndex("begin_time"))
                            var endTime = cursor.getString(cursor.getColumnIndex("end_time"))
                            var createdTime = cursor.getString(cursor.getColumnIndex("created_time"))
                            var id = cursor.getInt(cursor.getColumnIndex("id"))
                            firstCountDown = CountDown(id, target, createdTime, beginTime, endTime, order, final)

                        }
                    }


                } while (cursor.moveToNext())
            }

            cursor.close()

        }

    }

    override fun initViews() {
        val typeface = Typeface.createFromAsset(activity!!.assets, "icon_action.ttf")

        tvCountMenu.typeface = typeface


        if (showCountDown != null) {

            initCountDown()

        } else if (firstCountDown != null) {
            showCountDown = firstCountDown
            initCountDown()
        } else {
            tvTime.text = TimeUtils.curDay()
        }

    }

    override fun initListener() {
        tvCountMenu.setOnClickListener {
            startActivity(Intent(activity, CountDownListActivity::class.java))
        }
    }

    override fun initData() {

    }

    @Subscribe
    fun subscribe(first: TargetFirst) {
        showCountDown = first.countDown
        initCountDown()
    }

    @Subscribe
    fun subscribe(remove: TargetRemove) {
        showCountDown = null

        firstCountDown = null

        tvTarget.text = "未立下目标"
        tvTime.text = TimeUtils.curDay()

        tvDayCount.text = "0"

        tvHourCount.text = "0"
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}