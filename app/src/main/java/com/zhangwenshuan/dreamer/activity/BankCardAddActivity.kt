package com.zhangwenshuan.dreamer.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.BankCard
import com.zhangwenshuan.dreamer.bean.RemoveBank
import com.zhangwenshuan.dreamer.bean.RightBean
import com.zhangwenshuan.dreamer.custom.RightDialog
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.logInfo
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_bank_card_add.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class BankCardAddActivity : FinanceBaseActivity() {
    var isAdd = true

    var bankCard: BankCard? = null

    override fun setResourceId(): Int = R.layout.activity_bank_card_add

    override fun preInitData() {
        super.preInitData()

        var bundle = intent.getBundleExtra("data")

        bundle ?: return

        bankCard = bundle.getSerializable("bank") as BankCard

        if (bankCard != null) {
            isAdd = false
        }

    }

    override fun initViews() {

        tvAdd.visibility = View.VISIBLE

        tvAdd.typeface= Typeface.createFromAsset(assets,"icon_action.ttf")

        tvAdd.textSize = 18F

        if (isAdd) {
            tvTitle.text = "添加账户"
            tvAdd.text = resources.getString(R.string.ok)


        } else {
            tvTitle.text = "账户概览"

            tvAdd.text = resources.getString(R.string.delete)

            tvBankName.text = bankCard?.name

            etBankAccount.text = Editable.Factory.getInstance().newEditable(bankCard!!.account.toString())

            etBankNumber.text = Editable.Factory.getInstance().newEditable(bankCard!!.number)



            etBankNumber.isEnabled = false

            etBankAccount.isEnabled = false

            rlBankUpdate.visibility = View.VISIBLE


        }

        llBank.post {

            val titleBarAnimation =
                TranslateAnimation(llTitleBar.width.toFloat(), 0.toFloat(), -llTitleBar.height.toFloat(), 0.toFloat())

            titleBarAnimation.duration = 1000

            llTitleBar.startAnimation(titleBarAnimation)


            startAnimation(llBankAnimation, -llBankAnimation.height.toFloat(), 0.toFloat())


        }


    }

    var status = 0

    private fun startAnimation(view: View, formY: Float, toY: Float) {

        val alphaAnimation = AlphaAnimation(0.toFloat(), 1.toFloat())

        val animation = TranslateAnimation(rlBankLine.x, rlBankLine.x, formY, toY)

        val set = AnimationSet(true)


        set.addAnimation(alphaAnimation)


        set.addAnimation(animation)

        set.duration = 1000

        view.startAnimation(set)

        set.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if (status == 0) {
                    status = 1
                    startAnimation(llAccountAnimation, -llBankAnimation.height.toFloat(), 0.toFloat())
                    llAccountAnimation.visibility = View.VISIBLE
                } else if (status == 1) {
                    startAnimation(llNumberAnimation, -llBankAnimation.height.toFloat(), 0.toFloat())
                    llNumberAnimation.visibility = View.VISIBLE

                    status = -1
                }

            }

            override fun onAnimationStart(p0: Animation?) {
            }


        })

    }

    lateinit var bankList: MutableList<RightBean>


    override fun initListener() {

        val array = resources.getStringArray(R.array.bank_array)!!.toMutableList()

        bankList = mutableListOf()

        for (value in array) {
            bankList.add(RightBean(value))
        }


        tvBankName.setOnClickListener {

            if (isAdd) {
                val rightDialog = RightDialog(this@BankCardAddActivity, bankList!!)
                rightDialog.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        tvBankName.text = bankList!![position].title
                        rightDialog.dismiss()
                    }

                })
                rightDialog.show()
            }

        }

        tvAdd.setOnClickListener {
            if (!isAdd) {
                showDeleteBankCardDialog()
                return@setOnClickListener
            }


            toAdd()
        }


        tvBankBalanceEdit.setOnClickListener {
            val intent = Intent(this@BankCardAddActivity, BankAccountUpdateActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("bank", bankCard)

            intent.putExtra("data", bundle)

            startActivityForResult(intent, 1000)
        }

        tvBankFinanceDetail.setOnClickListener {
            val intent = Intent(this@BankCardAddActivity, FinanceDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("bank", bankCard)
            intent.putExtra("title", bankCard?.name)
            intent.putExtra("state", BANK_DETAIL)
            intent.putExtra("bankId", bankCard?.id)
            intent.putExtra("data", bundle)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                val account = data!!.getStringExtra("account")
                bankCard!!.account = account.toDouble()
                etBankAccount.text = Editable.Factory.getInstance().newEditable(account)
            }
        }
    }

    private fun toAdd() {
        val account = etBankAccount.text.toString()

        if (account == null || account == "") {
            toast("还有多少钱呢？")
            return
        }

        var number = etBankNumber.text.toString()

        if (number == null || number == "") {
            number = ""
        }



        NetUtils.data(NetUtils.getApiInstance().saveBank(
            BaseApplication.userId, tvBankName.text.toString(), account, number
        ), Consumer {
            logInfo(it.toString())


            if (it.code == 200) {
                toast("添加成功")

                finish()

                EventBus.getDefault().post(it.data)
            } else {
                toast("添加失败")
            }

        })
    }

    private fun toDelete() {
        NetUtils.data(NetUtils.getApiInstance().deleteBank(bankCard!!.id!!), Consumer {
            if (it.code == 200) {
                toast("删除成功")
                EventBus.getDefault().post(RemoveBank())
                finish()
            } else {
                toast("删除失败,请检查是否存在该收支明细")
            }


        })
    }

    private fun showDeleteBankCardDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("如果删除改账户，所有相关账单都一并删除！！！")
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    toDelete()
                }

            }).create()

        dialog.show()
    }

    override fun initData() {


    }
}