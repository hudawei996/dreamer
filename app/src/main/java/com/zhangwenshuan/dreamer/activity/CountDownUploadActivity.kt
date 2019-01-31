package com.zhangwenshuan.dreamer.activity

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.animation.*
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Countdown
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_count_down_sync.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import java.util.*

class CountDownUploadActivity : BaseActivity() {

    private var syncSate = -1

    private val pbStep = mutableListOf(0, 5, 10, 20, 40, 60, 80, 100)

    var querySql = "select * from dreamer where user_id=${BaseApplication.userId} order by oder asc"

    lateinit var reader: SQLiteDatabase

    private var localData = mutableListOf<Countdown>()
    private var serverData = mutableListOf<Countdown>()


    override fun setResourceId(): Int {
        return R.layout.activity_count_down_sync
    }

    override fun preInitData() {
        var helper = DBHelper(this)

        reader = helper.readableDatabase

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

        tvSync.typeface = typeface

        tvLocalData.text = localData.size.toString()
        tvLocalCount.text = localData.size.toString()


        var ringAnimation = AnimationUtils.loadAnimation(this, R.anim.count_down_ring)

        ring1.startAnimation(ringAnimation)
        ring2.startAnimation(ringAnimation)
        ring3.startAnimation(ringAnimation)
    }

    private fun startSyncAnimations() {
        var rotate = AnimationUtils.loadAnimation(this, R.anim.count_down_sync_tv)


        tvSync.startAnimation(rotate)


        tvSync.setTextColor(resources.getColor(R.color.colorPrimary))


        var animation = AnimationUtils.loadAnimation(this, R.anim.count_down_sync_animations)

        ivBg.visibility = View.VISIBLE

        ivBg.startAnimation(animation)

        dismissBottomViewAnimation()

        showSyncAnimation()
    }

    override fun initListener() {
        tvBack.setOnClickListener {
            finish()
        }

        tvSync.setOnClickListener {
            if (localData.size==0){
                toast("本地数据为0，无需同步到云端")
                return@setOnClickListener
            }


            startSyncAnimations()

            startProgressBarStep()

            tvSync.isClickable = false

            batchInsertData()
        }
    }

    private fun startProgressBarStep() {
        var step = 0
        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (step == pbStep.size) {
                        if (syncSate != -1) {
                            showResultView()

                            this.cancel()

                            return@runOnUiThread
                        }
                        step = 0

                    }
                    pbCountDown.progress = pbStep[step]
                    tvPbNumber.text = "${pbCountDown.progress}%"
                    step++
                }

            }
        }

        Timer().schedule(timerTask, 0, 300)
    }

    private fun showResultView() {
        var dismiss = AnimationUtils.loadAnimation(this, R.anim.count_down_dismiss_tv)

        tvSync.clearAnimation()


        dismiss.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                if (syncSate==0){
                    tvSync.text = resources.getString(R.string.dian_zan)
                    tvSync.setTextColor(resources.getColor(R.color.chart_color_1))
                    tvSync.textSize=40f

                    tvState.text = "同步完成"
                    tvState.setTextColor(resources.getColor(R.color.colorPrimary))
                }else{
                    tvSync.text = resources.getString(R.string.sorrow_face)
                    tvSync.setTextColor(resources.getColor(R.color.chart_color_5))
                    tvSync.textSize=40f

                    tvState.text = "同步失败"
                    tvState.setTextColor(resources.getColor(R.color.chart_color_5))
                }


                var show = AnimationUtils.loadAnimation(this@CountDownUploadActivity, R.anim.count_down_show_tv)


                tvSync.startAnimation(show)
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        tvSync.startAnimation(dismiss)





    }

    override fun initData() {

    }


    private fun dismissBottomViewAnimation() {
        var translate = TranslateAnimation(0f, 0f, 0f, llBottomData.y)

        var alpha = AlphaAnimation(1f, 0f)

        var set = AnimationSet(true)


        set.addAnimation(translate)

        set.addAnimation(alpha)

        set.duration = 1000

        set.fillAfter = true

        llBottomData.startAnimation(set)

        tvTime.startAnimation(set)


    }

    private fun showSyncAnimation() {
        // var translate = TranslateAnimation(0f, llBottomData.y, 0f, getScreenPoint().y.toFloat())

        var alpha = AlphaAnimation(0f, 1f)

        var set = AnimationSet(true)

        //    set.addAnimation(translate)

        set.addAnimation(alpha)

        set.duration = 2000

        set.fillAfter = true

        rlSyncState.visibility = View.VISIBLE

        rlSyncState.startAnimation(set)
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
                            BaseApplication.userId
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
                tvServerCount.text = serverData.size.toString()
                if (serverData.size>0){
                    tvTime.visibility=View.VISIBLE
                    tvTime.text="最近一次更新时间：${TimeUtils.getDay(serverData[0].uploadTime!!)}"
                }
            }
        })
    }

    private fun batchInsertData() {

        var json = GsonUtils.getGson().toJson(localData)

        var body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json)



        NetUtils.getApiInstance().insertCountdown(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                if (it.code == 200) {
                    syncSate = 0
                } else {
                    syncSate = 1
                   logError(it.message)
                }
            }, Consumer<Throwable> {
                it.printStackTrace()
                syncSate = 2
               logError(it.message+" ")
            })
    }
}