package com.zhangwenshuan.dreamer.activity

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.util.BaseApplication
import kotlinx.android.synthetic.main.activity_count_down_sync.*
import java.util.*

class CountDownSyncActivity : BaseActivity() {

    private val pbStep = mutableListOf(0, 5, 10, 20, 40, 60, 80, 100)

    override fun setResourceId(): Int {
        return R.layout.activity_count_down_sync
    }

    override fun preInitData() {


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
            startSyncAnimations()

            startProgressBarStep()

            tvSync.isClickable=false
        }
    }

    private fun startProgressBarStep() {
        var step = 0
        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (step == pbStep.size) step = 0
                    pbCountDown.progress = pbStep[step]
                    tvPbNumber.text="${pbCountDown.progress}%"
                    step++
                }

            }
        }

        Timer().schedule(timerTask,0,300)
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
}