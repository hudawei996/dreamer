package com.zhangwenshuan.dreamer.activity

import android.text.Editable
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.custom.RightDialog
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance_add.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import java.util.*


class FinanceAddActivity : FinanceBaseActivity() {
    override fun setResourceId(): Int = R.layout.activity_finance_add

    var accountHint = "金额必填哦"

    var typeHint = "类别必填哦"

    var isExpense = 1

    lateinit var bank: BankCard

    var finance: Finance? = null

    var list: MutableList<BankCard> = mutableListOf()

    override fun initViews() {
        if (finance == null) {
            tvTitle.text = "添加账单"

            tvFinanceTimeAdd.text = TimeUtils.curTime()

        } else {
            tvTitle.text = "账单详细"

            tvFinanceAddAgain.text = "删除"

            tvFinanceAdd.text = "更新"

            tvFinanceAdd.visibility = View.GONE

            vLineCenter.visibility = View.GONE

            tvFinanceBankAdd.text = finance?.bankName

            etFinanceNameAdd.text = Editable.Factory.getInstance().newEditable(finance?.type)

            etFinanceAccountAdd.text =
                    Editable.Factory.getInstance().newEditable(decimalFormat.format(finance?.account))

            etFinanceRemarkAdd.text = Editable.Factory.getInstance().newEditable(finance?.remark)

            tvFinanceTimeAdd.text = finance?.date + finance?.time


            isExpense = finance!!.isExpense

            if (isExpense == 1) {
                tvFinanceTypeAdd.text = "支出"
            } else {
                tvFinanceTypeAdd.text = "收入"
            }

        }
    }

    var saveAgain = false

    override fun preInitData() {
        super.preInitData()

        val data = intent.getBundleExtra("data")



        if (data != null) {
            finance = data.getSerializable("finance") as Finance

        }
    }

    override fun initListener() {
        tvFinanceTimeAdd.setOnClickListener {
            showTimePickerView()
        }

        tvFinanceAdd.setOnClickListener {
            saveAgain = false

            if (finance != null) {
                toUpdateFinance()
            } else {
                toSaveFinance()
            }
        }

        tvFinanceBankAdd.setOnClickListener {
            toShowBank()
        }

        tvFinanceAddAgain.setOnClickListener {
            saveAgain = true
            if (finance == null) {
                toSaveFinance()
            } else {
                toDelete()
            }
        }

        tvFinanceTypeAdd.setOnClickListener {
            if (isExpense == 0) {
                isExpense = 1
                tvFinanceTypeAdd.text = "支出"
            } else {
                isExpense = 0
                tvFinanceTypeAdd.text = "收入"
            }
            tvFinanceTypeAdd.setTextColor(resources.getColor(R.color.colorBlack))
        }


    }

    private fun toUpdateFinance() {

    }

    private fun toDelete() {
        NetUtils.data(NetUtils.getApiInstance().deleteFinance(finance!!.id!!), Consumer {
            if (it.code == 200) {
                EventBus.getDefault().post(FinanceDelete(finance!!))
                finish()
            }
            toast(it.message)

        })
    }

    private fun toShowBank() {

        val dialog = RightDialog(this, list!!)

        dialog.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                tvFinanceBankAdd.text = list!![position].name

                tvFinanceBankAdd.setTextColor(resources.getColor(R.color.colorBlack))

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
                    tvFinanceTimeAdd.text = TimeUtils.dateToString(date!!)
                    tvFinanceTimeAdd.setTextColor(resources.getColor(R.color.colorBlack))
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
        toGetBank()
    }

    private fun toGetBank() {
        NetUtils.data(NetUtils.getApiInstance().getBank(BaseApplication.userId), Consumer {
            if (it.code == 200) {
                list.addAll(it.data)

                bank = list[0]
                tvFinanceBankAdd.text = list[0].name

            }


        })

    }


    private fun toSaveFinance() {

        val type = etFinanceNameAdd.text.toString()

        if (type == "") {
            toast(typeHint)
            return
        }

        var account = etFinanceAccountAdd.text.toString()

        if (account == "") {
            toast(accountHint)
            return
        }

        val time = tvFinanceTimeAdd.text.toString()

        val remark = etFinanceRemarkAdd.text.toString()



        NetUtils.data(
            NetUtils.getApiInstance().saveFinance(
                BaseApplication.userId,
                type, time, account, remark, bank!!.name, bank!!.id!!, isExpense
            ),
            Consumer {

                toast("${it.message}")

                if (it.code == 200) {

                    EventBus.getDefault().post(FinanceAdd(it.data))

                    if (saveAgain) {
                        etFinanceAccountAdd.text = Editable.Factory.getInstance().newEditable("")
                        etFinanceNameAdd.text = Editable.Factory.getInstance().newEditable("")
                        etFinanceRemarkAdd.text = Editable.Factory.getInstance().newEditable("")

                    } else {

                        finish()
                    }


                }

                toast(it.message)


            }
        )
    }


}