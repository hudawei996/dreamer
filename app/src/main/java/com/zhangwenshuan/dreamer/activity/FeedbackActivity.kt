package com.zhangwenshuan.dreamer.activity

import android.graphics.Typeface
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.jetbrains.anko.toast

class FeedbackActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_feedback
    }

    override fun preInitData() {
        tvTitle.text = "在线反馈"

        tvAdd.text = resources.getString(R.string.ok)

        tvAdd.visibility= View.VISIBLE

        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")
    }

    override fun initViews() {
    }

    override fun initListener() {

        tvAdd.setOnClickListener {
            val text=etFeedback.text.toString()

            if (text.isEmpty()){
                toast("反馈内容不能为空")
                return@setOnClickListener
            }


            NetUtils.data(NetUtils.getApiInstance().saveFeedback(BaseApplication.userId,text), Consumer {
                toast(it.message)
                if (it.code==200){
                    finish()
                }
            })
        }
    }

    override fun initData() {
    }
}