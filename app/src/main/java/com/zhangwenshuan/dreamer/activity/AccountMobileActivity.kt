package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Bank
import com.zhangwenshuan.dreamer.bean.BankAdd1
import com.zhangwenshuan.dreamer.bean.BankUpdate
import com.zhangwenshuan.dreamer.bean.MobileAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_account_moblie.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class AccountMobileActivity : FinanceBaseActivity() {

    lateinit var bank: Bank

    var update = false

    override fun setResourceId(): Int {
        return R.layout.activity_account_moblie
    }

    override fun initViews() {
        val data = intent.getBundleExtra("data")

        if (data != null) {
            bank = data.getSerializable("bank") as Bank

            tvTitle.text = bank.name

            etMoBileName.text = Editable.Factory.getInstance().newEditable(bank.name)

            if (bank.username != null) {
                etMobileUsername.text = Editable.Factory.getInstance().newEditable(bank.username)
            }

            etMobileAccount.text = Editable.Factory.getInstance().newEditable(decimalFormat.format(bank.account))

            if (bank.username != null) {
                etMobileRemark.text = Editable.Factory.getInstance().newEditable(bank.remark)
            }

            update = true

        } else {
            tvTitle.text = "添加移动支付"
        }



        tvAdd.text = resources.getString(R.string.ok)

        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

        tvAdd.visibility = View.VISIBLE

    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            if (update) {
                toUpdateMobile()
            } else {
                toAddMobile()
            }

        }
    }

    private fun toUpdateMobile() {
        val name = etMoBileName.text.toString()

        if (name.isEmpty()) {
            toast("账户名称不能为空")
            return
        }

        val account = etMobileAccount.text.toString().replace(",", "")

        if (account.isEmpty()) {
            toast("余额不能为空")
            return
        }

        val username = etMobileUsername.text.toString()

        val remark = etMobileRemark.text.toString()

        bank.name = name

        bank.username = username

        bank.remark = remark

        bank.account = account.toDouble()



        NetUtils.data(
            NetUtils.getApiInstance().updateMobile(
                bank.id,
                name,
                username,
                account,
                remark
            ), Consumer {
                toast(it.message)

                if (it.code == 200) {
                    EventBus.getDefault().post(BankUpdate(bank))
                    finish()
                }
            })
    }

    private fun toAddMobile() {
        val name = etMoBileName.text.toString()

        if (name.isEmpty()) {
            toast("账户名称不能为空")
            return
        }

        val account = etMobileAccount.text.toString()

        if (account.isEmpty()) {
            toast("余额不能为空")
            return
        }

        NetUtils.data(
            NetUtils.getApiInstance().saveMobile(
                BaseApplication.userId, name, account,
                etMobileUsername.text.toString(),
                etMobileRemark.text.toString()
            ), Consumer {
                toast(it.message)

                if (it.code == 200) {
                    EventBus.getDefault().post((MobileAdd(it.data)))
                    finish()
                }
            })
    }

    override fun initData() {

    }
}