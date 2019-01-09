package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.CashAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_account_cash.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class AccountCashActivity : FinanceBaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_account_cash
    }

    override fun initViews() {

        tvAdd.text = resources.getString(R.string.ok)

        val typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

        tvAdd.typeface=typeface

        tvAdd.visibility = View.VISIBLE

        tvTitle.text = "添加现金"

        tvCashClear.text=resources.getString(R.string.clear)
        tvCashClear.typeface=typeface
    }

    override fun initListener() {

        tvAdd.setOnClickListener {
            toSaveCash()
        }

        tvCashClear.setOnClickListener {
            etCashAccount.text=Editable.Factory.getInstance().newEditable("")
        }
    }

    private fun toSaveCash() {
        var account = etCashAccount.text.toString()

        if (account.isEmpty()) {
            toast("余额不能为空")
            return
        }

        account = account.replace(",", "")

        NetUtils.data(NetUtils.getApiInstance().saveCash(
            BaseApplication.userId,
            "现金",
            account,
            etCashRemark.text.toString()
        ),
            Consumer {
                toast(it.message)

                if (it.code == 200) {
                    EventBus.getDefault().post(CashAdd(it.data))
                    finish()
                }
            })
    }

    override fun initData() {
        NetUtils.data(NetUtils.getApiInstance().getCash(
            BaseApplication.userId
        ),
            Consumer {

                if (it.code == 200) {
                    etCashAccount.text =
                            Editable.Factory.getInstance().newEditable(decimalFormat.format(it.data.account))
                    etCashRemark.text =
                            Editable.Factory.getInstance().newEditable(it.data.remark)
                } else {
                    toast(it.message)
                }
            })
    }
}