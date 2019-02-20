package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Bank
import com.zhangwenshuan.dreamer.bean.BankUpdate
import com.zhangwenshuan.dreamer.bean.CashAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.decimalFormat
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_account_cash.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class AccountCashActivity : FinanceBaseActivity() {

    private var isUpdate=false

    override fun setResourceId(): Int {
        return R.layout.activity_account_cash
    }

    override fun initViews() {

        isUpdate=intent.getBooleanExtra("update",false)

        tvAdd.text = resources.getString(R.string.ok)

        val typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

        tvAdd.typeface=typeface

        tvAdd.visibility = View.VISIBLE

        if (isUpdate){

            tvTitle.text = "现金"
        }else{

            tvTitle.text = "添加现金"
        }


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


                if (it.code == 200) {
                    if(isUpdate){
                        EventBus.getDefault().post(
                            BankUpdate(Bank(name="现金",account = account.toDouble(),
                                remark =etCashRemark.text.toString(),type ="cash" )))
                        toast("添加成功")
                    }else{
                        EventBus.getDefault().post(CashAdd(it.data))
                        toast("保存成功")
                    }
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