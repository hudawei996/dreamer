package com.zhangwenshuan.dreamer.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.google.gson.reflect.TypeToken
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.custom.RightDialog
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance_expense.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import java.util.*


class FinanceAddActivity : BaseActivity() {
    override fun setResourceId(): Int = R.layout.activity_finance_expense

    var accountHint = "花了多少呢？"

    var typeHint = "花在哪呢？"

    var isExpense = 1

    var bank: BankCard? = null

     var list: MutableList<BankCard>?=null

    override fun preInitData() {
        isExpense = intent.getIntExtra("isExpense", 1)

        list = getBankCarsFromLocal()

        if (list ==null) {

            showAddBankCardDialog()

            return
        }



    }

    override fun initViews() {

        if (isExpense == 0) {
            tvTitle.text = "哇，又收入呀"

            typeHint = "钱从哪来呢"

            accountHint = "赚了多少钱呢"

            tvTimeHint.text = "什么时候赚的:"
            tvAccountHint.text = "赚了多少:"
            tvTypeHint.text = "从哪里赚的:"
            tvFinanceBankHint.text = "放哪呢"

            tvExpenseType.hint = "工资"
        } else {
            tvTitle.text = "又花钱了"
        }

        tvFinanceBank.text = list!![0].name

        bank = list!![0]

        tvExpenseTime.text = TimeUtils.curTime()
    }

    override fun initListener() {
        tvExpenseTime.setOnClickListener {
            showTimePickerView()
        }

        tvExpenseAdd.setOnClickListener {
            toSaveExpense()
        }

        tvFinanceBank.setOnClickListener {
            toShowBank()
        }
    }

    private fun toShowBank() {

        val dialog = RightDialog(this, list!!)

        dialog.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                tvFinanceBank.text = list!![position].name

                bank = list!![position]

                dialog.dismiss()
            }
        })

        dialog.show()
    }

    private fun showTimePickerView() {
        val arrays = arrayOf(true, true, true, true, true, false)

        val pvTime = TimePickerBuilder(this@FinanceAddActivity, object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                if (date != null) {
                    tvExpenseTime.text = TimeUtils.dateToString(date!!)
                    tvExpenseTime.setTextColor(resources.getColor(R.color.colorBlack))
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

    override fun initData() {
    }


    private fun toSaveExpense() {

        val type = tvExpenseType.text.toString()

        if (type == "") {
            toast(typeHint)
            return
        }

        var account = tvExpenseAccount.text.toString()

        if (account == "") {
            toast(accountHint)
            return
        }

        val time = tvExpenseTime.text.toString()

        val remark = tvExpenseRemark.text.toString()



        NetUtils.data(
            NetUtils.getApiInstance().saveFinance(
                BaseApplication.userId,
                type, time, account, remark, bank!!.name, bank!!.id!!, isExpense
            ),
            Consumer {

                toast("${it.message}")

                if (it.code == 200) {
                    if (isExpense == 1) {
                        EventBus.getDefault().post(EventBean(EVENT_SAVE_EXPENSE, account))
                    } else {
                        EventBus.getDefault().post(EventBean(EVENT_SAVE_INCOME, account))
                    }


                    finish()
                }


            }
        )
    }

    fun showAddBankCardDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("请先添加账户")
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    finish()
                }

            }).create()

        dialog.show()
    }


}