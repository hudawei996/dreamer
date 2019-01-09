package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Bank
import com.zhangwenshuan.dreamer.bean.BankUpdate
import com.zhangwenshuan.dreamer.bean.CreditAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_account_credit.*
import kotlinx.android.synthetic.main.activity_finance_add.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class AccountCreditActivity : FinanceBaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_account_credit
    }

    lateinit var bank: Bank

    var billDate = false

    var update = false

    override fun initViews() {

        val data = intent.getBundleExtra("data")

        if (data != null) {
            bank = data.getSerializable("bank") as Bank

            etCreditAmount.text = Editable.Factory.getInstance().newEditable(decimalFormat.format(bank.amount))

            etCreditName.text = Editable.Factory.getInstance().newEditable(bank.name)

            etCreditNumber.text = Editable.Factory.getInstance().newEditable(bank.number)

            etCreditDebt.text = Editable.Factory.getInstance().newEditable(decimalFormat.format(bank.debt))

            etCreditRemark.text = Editable.Factory.getInstance().newEditable(bank.remark)

            tvCreditReturnDate.text = bank.returnDate

            tvCreditBillDate.text = bank.billDate

            update = true

            tvTitle.text = "信用卡详情"
        } else {

            tvTitle.text = "添加信用卡"
        }

        tvAdd.text = resources.getString(R.string.ok)
        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")
        tvAdd.visibility = View.VISIBLE


    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            if (update) {
                toUpdateCredit()
            } else {
                toSaveCredit()
            }
        }

        tvCreditBillDate.setOnClickListener {
            billDate = true
            showDateDialog()
        }

        tvCreditReturnDate.setOnClickListener {
            billDate = false
            showDateDialog()
        }
    }

    private fun toUpdateCredit() {
        val name = etCreditName.text.toString()

        if (name.isEmpty()) {
            toast("账户名不能为空")
            return
        }

        val amount = etCreditAmount.text.toString().replace(",","")

        if (amount.isEmpty()) {
            toast("额度不能为空")
            return
        }

        var debt = etCreditDebt.text.toString().replace(",","")

        if (debt.isEmpty()) {
            toast("欠款不能为空")
            return
        }

        if (amount.toDouble() - debt.toDouble() < 0) {
            toast("欠款不能大于额度")
            return
        }

        val remark=etCreditRemark.text.toString()

        val number=etCreditNumber.text.toString()

        val returnDate=tvCreditReturnDate.text.toString()

        val billDate=tvCreditBillDate.text.toString()

        bank.debt=debt.toDouble()

        bank.name=name

        bank.amount=amount.toDouble()

        bank.remark=remark

        bank.number=number

        bank.returnDate=returnDate

        bank.billDate=billDate

        NetUtils.data(NetUtils.getApiInstance().updateCredit(
            bank.id, amount, debt,
            billDate,
            returnDate,
            name, number, remark
        ), Consumer {
            toast(it.message)

            if (it.code == 200) {
                EventBus.getDefault().post(BankUpdate(bank))
                finish()
            }
        })
    }


    private fun toSaveCredit() {
        val name = etCreditName.text.toString()

        if (name.isEmpty()) {
            toast("账户名不能为空")
            return
        }

        val amount = etCreditAmount.text.toString()

        if (amount.isEmpty()) {
            toast("额度不能为空")
            return
        }

        val debt = etCreditDebt.text.toString()

        if (debt.isEmpty()) {
            toast("欠款不能为空")
            return
        }

        if (amount.toDouble() - debt.toDouble() < 0) {
            toast("欠款不能大于额度")
            return
        }

        NetUtils.data(NetUtils.getApiInstance().saveCredit(
            BaseApplication.userId,
            name,
            amount,
            debt,
            tvCreditBillDate.text.toString(),
            tvCreditReturnDate.text.toString(),
            etCreditRemark.text.toString(),
            etCreditNumber.text.toString()
        ),
            Consumer {
                toast(it.message)

                if (it.code == 200) {
                    EventBus.getDefault().post(CreditAdd(it.data))
                    finish()
                }
            })
    }

    private fun showDateDialog() {
        val incomes = resources.getStringArray(R.array.date).toMutableList()

        val view = OptionsPickerBuilder(this,
            OnOptionsSelectListener { options1, options2, options3, v ->
                if (billDate) {
                    tvCreditBillDate.text = incomes[options1]
                } else {
                    tvCreditReturnDate.text = incomes[options1]
                }
            })
            .setContentTextSize(18)
            .setSelectOptions(15)
            .build<String>()

        view.setPicker(incomes)

        view.show()
    }

    override fun initData() {

    }
}