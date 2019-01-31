package com.zhangwenshuan.dreamer.activity

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Countdown
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.DBHelper
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_count_down_download.*
import org.jetbrains.anko.toast
import java.util.*

class CountDownDownloadActivity : BaseActivity() {

    private var syncSate = -1

    var querySql = "select * from dreamer where user_id=${BaseApplication.userId} order by oder asc"

    lateinit var reader: SQLiteDatabase


    lateinit var writer: SQLiteDatabase

    private var localData = mutableListOf<Countdown>()
    private var serverData = mutableListOf<Countdown>()


    override fun setResourceId(): Int {
        return R.layout.activity_count_down_download
    }

    override fun preInitData() {
        var helper = DBHelper(this)

        reader = helper.readableDatabase

        writer = helper.writableDatabase

        localData.addAll(queryData(querySql))

        queryServerData()
    }


    override fun initViews() {
        window.statusBarColor = Color.TRANSPARENT

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        var layoutParams = vTop.layoutParams

        layoutParams.height = BaseApplication.getStatusHeight()

        vTop.layoutParams = layoutParams


        var typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

        tvBack.typeface = typeface

        tvPhone.typeface = typeface

        tvServer.typeface = typeface

        tvDownload.typeface = typeface

        tvLeft.typeface = typeface

        tvRight.typeface = typeface

        tvLocalData.text = localData.size.toString()

        tvPhone.post {
            showDownloadAnimation()
        }


    }


    var downY = 0f

    var serverRawY = 0f

    var downTime = 0L

    var delay = 100

    var drag = true

    override fun initListener() {
        tvBack.setOnClickListener {
            finish()
        }


        tvServer.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (drag) {

                    when (event!!.action) {
                        MotionEvent.ACTION_DOWN -> {
                            downY = event!!.y
                            downTime = System.currentTimeMillis()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (System.currentTimeMillis() - downTime > delay) {
                                var offsetY = event!!.y - downY

                                var serverY = offsetY + tvServer.y

                                if (serverY + tvServer.height > tvPhone.y + tvServer.height / 2) {
                                    drag = false

                                    tvDownload.clearAnimation()

                                    tvDownload.visibility = View.GONE

                                    tvServer.visibility = View.GONE

                                    tvTime.text = "正在同步本地数据"

                                    showSyncDownloadAnimation()

                                }

                                if (serverY < serverRawY) {
                                    serverY = serverRawY
                                }

                                tvServer.y = serverY
                            }
                        }

                    }
                }

                return true
            }
        })
    }


    override fun initData() {

    }


    private fun showSyncDownloadAnimation() {
        var scale = AnimationUtils.loadAnimation(this, R.anim.count_down_download_animations)

        tvPhone.startAnimation(scale)


        scale.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                var translateAnimation = TranslateAnimation(0f, (tvLeft.width).toFloat() / 2, 0f, 0f)

                translateAnimation.repeatCount = Animation.INFINITE

                translateAnimation.repeatMode = Animation.RESTART

                translateAnimation.duration = 1200

                translateAnimation.interpolator = AccelerateInterpolator()

                var rightAnimation = TranslateAnimation(0f, -tvRight.width.toFloat() / 2, 0f, 0f)

                rightAnimation.repeatCount = Animation.INFINITE

                rightAnimation.repeatMode = Animation.RESTART

                rightAnimation.duration = 1200

                rightAnimation.interpolator = AccelerateInterpolator()

                tvRight.startAnimation(rightAnimation)

                tvLeft.startAnimation(translateAnimation)

                batchInsertData()


                translateAnimation.setAnimationListener(object:Animation.AnimationListener{
                    override fun onAnimationRepeat(animation: Animation?) {
                        if (writeState==1){
                            animation!!.cancel()
                            tvRight.clearAnimation()
                            tvLeft.visibility=View.GONE
                            tvRight.visibility=View.GONE
                            localData.clear()
                            localData.addAll(queryData(querySql))
                            tvLocalData.text=localData.size.toString()
                            if (localData.size>0){
                                tvTime.text="最近一次同步时间：${TimeUtils.getDay(localData[0].uploadTime!!)}"
                            }

                            tvPhone.text=resources.getString(R.string.dian_zan)


                        }else{
                            tvPhone.text=resources.getString(R.string.sorrow_face)
                            tvLeft.visibility=View.GONE
                            tvRight.visibility=View.GONE
                            tvTime.text="同步到本地失败"
                        }
                    }

                    override fun onAnimationEnd(animation: Animation?) {

                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                })
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })


        tvLeft.visibility = View.VISIBLE

        tvRight.visibility = View.VISIBLE


    }

    private fun showDownloadAnimation() {

        serverRawY = tvServer.y

        var toY = (tvPhone.y - tvServer.y - tvServer.height / 2)

        var formY = tvServer.height

        var translate = TranslateAnimation(0f, 0f, formY.toFloat() / 2, toY)

        translate.duration = 3000

        var alpha = AlphaAnimation(1f, 0.5f)

        var set = AnimationSet(false)

        set.addAnimation(translate)

        set.addAnimation(alpha)

        translate.repeatCount = Animation.INFINITE

        translate.repeatMode = Animation.RESTART

        set.fillAfter = true

        tvDownload.startAnimation(set)


    }


    private fun queryData(sql: String): MutableList<Countdown> {
        var data = mutableListOf<Countdown>()

        var cursor = reader.rawQuery(sql, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    var target = cursor.getString(cursor.getColumnIndex("target"))
                    var beginTime = cursor.getString(cursor.getColumnIndex("begin_time"))
                    var endTime = cursor.getString(cursor.getColumnIndex("end_time"))
                    var createdTime = cursor.getString(cursor.getColumnIndex("created_time"))
                    var final = cursor.getInt(cursor.getColumnIndex("success"))
                    var order = cursor.getInt(cursor.getColumnIndex("oder"))
                    var id = cursor.getInt(cursor.getColumnIndex("id"))
                    var show = cursor.getInt(cursor.getColumnIndex("show"))
                    var uploadTime = cursor.getLong(cursor.getColumnIndex("upload_time"))

                    data.add(
                        Countdown(
                            id,
                            target,
                            createdTime,
                            beginTime,
                            endTime,
                            order,
                            final,
                            show,
                            BaseApplication.userId,
                            Date(uploadTime)
                        )
                    )

                } while (cursor.moveToNext())
            }

            cursor.close()

        }

        return data
    }

    private fun queryServerData() {
        NetUtils.data(NetUtils.getApiInstance().getCountdown(BaseApplication.userId), Consumer {
            if (it.code == 200) {
                serverData.addAll(it.data)
                tvServerData.text = serverData.size.toString()
                if (serverData.size > 0) {
                    tvTime.visibility = View.VISIBLE
                    tvTime.text = "最近一次更新时间：${TimeUtils.getDay(serverData[0].uploadTime!!)}"
                }
            }
        })
    }


    private var writeState=-1

    private fun batchInsertData() {
       val sb=StringBuilder()
        sb.append("insert into dreamer(user_id,target,begin_time,end_time,created_time" +
                ",show,oder,success,upload_time) values ")

        for ((index,value) in serverData.withIndex()){
            val data="(${BaseApplication.userId},'${value.target}','${value.beginTime}','${value.endTime}'," +
                    "'${value.createdTime}',${value.show},${value.order},${value.success},${System.currentTimeMillis()})"

            sb.append(data)

            if (index<serverData.size-1){
                sb.append(",")
            }
        }


        writer.beginTransaction()

        try {
            if (localData.size>0){
                writer.delete("dreamer","user_id=${BaseApplication.userId}",null)
            }

            var sql=sb.toString()

            writer.execSQL(sql)

            writer.setTransactionSuccessful()

            writeState=1
        }catch(e:Exception){

            writeState=0

            e.printStackTrace()
        }finally {
            writer.endTransaction()
        }

    }
}