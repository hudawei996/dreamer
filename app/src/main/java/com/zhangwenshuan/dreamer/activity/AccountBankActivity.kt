package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Bank
import com.zhangwenshuan.dreamer.bean.BankAdd1
import com.zhangwenshuan.dreamer.bean.BankUpdate
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_account_bank.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class AccountBankActivity : FinanceBaseActivity() {

    override fun setResourceId(): Int {
        return R.layout.activity_account_bank
    }

    lateinit var bank: Bank

    var update = false

    override fun preInitData() {
        super.preInitData()

        val data = intent.getBundleExtra("data")

        if (data != null) {
            bank = data.getSerializable("bank") as Bank

            etBankName.text = Editable.Factory.getInstance().newEditable(bank.name)

            etBankNumber.text = Editable.Factory.getInstance().newEditable(bank.number)

            etBankAccount.text = Editable.Factory.getInstance().newEditable(decimalFormat.format(bank.account))

            etBankRemark.text = Editable.Factory.getInstance().newEditable(bank.remark)

            update = true
        }
    }

    override fun initViews() {

        tvAdd.text = resources.getString(R.string.ok)

        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

        tvAdd.visibility = View.VISIBLE

        if (update) {
            tvTitle.text = "银行卡详情"
        } else {
            tvTitle.text = "添加银行卡"
        }
    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            if (update) {
                toUpdateBank()
            } else {
                toAddBank()
            }
        }
    }

    private fun toUpdateBank() {
        val name = etBankName.text.toString()

        if (name.isEmpty()) {
            toast("账户名称不能为空")
            return
        }

        var account = etBankAccount.text.toString()

        if (account.isEmpty()) {
            toast("余额不能为空")
            return
        }

        account = account.replace(",", "")

        val remark=etBankRemark.text.toString()


        val number=etBankNumber.text.toString()
        bank.account=account.toDouble()

        bank.number=number

        bank.remark=remark

        bank.name=name


        NetUtils.data(NetUtils.getApiInstance().updateBank(
            bank.id, account, name,
            remark,number
        ), Consumer {
            toast(it.message)
            if (it.code == 200) {
                EventBus.getDefault().post(BankUpdate(bank))
                finish()
            }
        })
    }

    private fun toAddBank() {
        val name = etBankName.text.toString()

        if (name.isEmpty()) {
            toast("账户名称不能为空")
            return
        }

        val account = etBankAccount.text.toString()

        if (account.isEmpty()) {
            toast("余额不能为空")
            return
        }


        NetUtils.data(NetUtils.getApiInstance().saveBank(
            BaseApplication.userId, name, account,
            etBankNumber.text.toString(),
            etBankRemark.text.toString()
        ), Consumer {
            toast(it.message)

            if (it.code == 200) {
                EventBus.getDefault().post((BankAdd1(it.data)))
                startActivity(Intent(this@AccountBankActivity, FinanceSynopsisActivity::class.java))
                finish()
            }
        })
    }

    override fun initData() {
    }
}