package com.zhangwenshuan.dreamer.activity

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.BookAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import com.zhangwenshuan.dreamer.util.logError
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_book_add.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import java.util.*

class BookAddActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_book_add
    }

    override fun preInitData() {
    }

    override fun initViews() {
        tvTitle.text = "读书记录"

        tvAdd.visibility = View.VISIBLE

        tvBookBegin.text = TimeUtils.curDay()
    }


    var isBeginTime = true

    override fun initListener() {
        tvAdd.setOnClickListener {
            toSave()
        }

        tvBookBegin.setOnClickListener {
            isBeginTime = true
            showDatePickerDialog()
        }

        tvBookEnd.setOnClickListener {
            isBeginTime = false
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        closeInput()
        val arrays = arrayOf(true, true, true, false, false, false)

        val pvTime = TimePickerBuilder(this@BookAddActivity, object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                if (date != null) {

                    if (isBeginTime) {

                        tvBookBegin.text = TimeUtils.getDay(date!!)
                    } else {
                        tvBookEnd.text = TimeUtils.getDay(date!!)

                    }

                }


            }
        })
            .setType(arrays.toBooleanArray())
            .setSubmitColor(resources.getColor(R.color.colorPrimary))//确定按钮文字颜色
            .setCancelColor(resources.getColor(R.color.colorPrimary))//取消按钮文字颜色
            .setTextColorCenter(resources.getColor(R.color.colorPrimary))
            .build()

        pvTime.show()

    }


    private fun closeInput() {

        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        if(imm != null) {
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }

    private fun toSave() {

        val bookName = etBookName.text.toString()

        if (bookName.isEmpty()) {
            toast("读了什么书呢")
            return
        }

        logError("---------")
        logError(BaseApplication.token)

        NetUtils.data(NetUtils.getApiInstance().saveBook(
            BaseApplication.userId,
            bookName, tvBookBegin.text.toString(), tvBookEnd.text.toString(),
            etBookContent.text.toString(), etBookEvaluate.text.toString()
        ), Consumer {
            if (it.code == 200) {
                EventBus.getDefault().post(BookAdd())
                finish()
            }
            toast(it.message)
        })


    }

    override fun initData() {
    }
}