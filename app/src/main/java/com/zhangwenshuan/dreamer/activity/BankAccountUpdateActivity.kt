package com.zhangwenshuan.dreamer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.BankCard
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.logError
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_bank_account_update.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.jetbrains.anko.toast

class BankAccountUpdateActivity : FinanceBaseActivity() {
    override fun setResourceId(): Int = R.layout.activity_bank_account_update


    lateinit var bank: BankCard

    override fun preInitData() {
        super.preInitData()
        bank = intent.getBundleExtra("data").getSerializable("bank") as BankCard
    }

    override fun initViews() {
        tvTitle.text = "修改金额"

        tvAdd.visibility = View.VISIBLE

        tvAdd.text = resources.getString(R.string.ok)

        tvAdd.typeface= Typeface.createFromAsset(assets,"icon_action.ttf")

        etUpdateAccount.text=Editable.Factory.getInstance().newEditable(bank.account.toString())
    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            val account = etUpdateAccount.text.toString()

            if (account.isEmpty()) {
                toast("金额不能为空")
                return@setOnClickListener
            }


            NetUtils.data(NetUtils.getApiInstance().updateBank(bank.id!!, account), Consumer {


                if (it.code == 200) {
                    val intent = Intent()
                    intent.putExtra("account", account)
                    setResult(Activity.RESULT_OK, intent)
                    toast("修改成功")
                    finish()
                } else {
                    toast("修改失败")
                    logError(it.toString())
                }
            }
            )
        }
    }

    override fun initData() {

    }
}