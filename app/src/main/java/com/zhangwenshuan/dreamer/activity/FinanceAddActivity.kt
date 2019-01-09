package com.zhangwenshuan.dreamer.activity

import android.graphics.Typeface
import android.text.Editable
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.custom.RightDialog
import com.zhangwenshuan.dreamer.util.*
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

    lateinit var bank: Bank

    var finance: Finance? = null

    var list: MutableList<String> = mutableListOf()

    var banks = mutableListOf<Bank>()

    var update = false

    override fun initViews() {
        if (finance == null) {
            tvTitle.text = "添加账单"

            tvFinanceTimeAdd.text = TimeUtils.curTime()


        } else {
            tvTitle.text = "账单详细"

            update = true

            tvFinanceAddAgain.text = "删除"

            tvAdd.text = resources.getString(R.string.ok)

            tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

            tvAdd.visibility=View.VISIBLE

            tvFinanceAdd.visibility = View.GONE

            vLineCenter.visibility = View.GONE

            tvFinanceBankAdd.text = finance?.bankName



            etFinanceAccountAdd.text =
                    Editable.Factory.getInstance().newEditable(decimalFormat.format(finance?.account))

            etFinanceRemarkAdd.text = Editable.Factory.getInstance().newEditable(finance?.remark)

            tvFinanceTimeAdd.text = finance?.date +" "+ finance?.time


            isExpense = finance!!.isExpense

            if (isExpense == 1) {
                tvFinanceTypeAdd.text = "支出"

                tvFinanceItemAdd.text = finance?.item + " " + finance?.type
            } else {
                tvFinanceTypeAdd.text = "收入"

                tvFinanceItemAdd.text = finance?.item
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


            toSaveFinance()

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
                tvFinanceItemAdd.text = "餐饮 午餐"
            } else {
                isExpense = 0
                tvFinanceTypeAdd.text = "收入"
                tvFinanceItemAdd.text = "红包"
            }
            tvFinanceTypeAdd.setTextColor(resources.getColor(R.color.colorBlack))
        }

        tvFinanceItemAdd.setOnClickListener {
            TimeUtils.closeInput(this)
            if (isExpense == 1) {
                showItemName()
            } else {
                showIncomeDialog()
            }
        }

        tvAdd.setOnClickListener {
            toUpdateFinance()
        }


    }

    private fun toUpdateFinance() {
        val type = tvFinanceItemAdd.text.toString()

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


        if (bank == null) {
            toast("请先添加用户")
            return
        }

        val strs = time.split(" ")

        finance?.date = strs[0]

        finance?.time = strs[1]

        finance?.account = account.replace(",", "").toDouble()


        finance?.bankName = bank!!.name!!

        finance?.bankId = bank!!.id!!

        finance?.isExpense = isExpense

        if (isExpense==0){
            finance?.item=type
            finance?.type=type
        }else{
           val str= type.split(" ")
            finance!!.item=str[0]
            finance!!.type=str[1]
        }


        NetUtils.data(
            NetUtils.getApiInstance().updateFinance(
                finance!!.id!!,
                type, time, finance!!.account!!, remark, bank!!.name!!, bank!!.id!!, isExpense
            ),
            Consumer {

                toast("${it.message}")

                if (it.code == 200) {


                    EventBus.getDefault().post(FinanceUpdate(finance!!))


                    finish()

                }


            }
        )
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
        val view = OptionsPickerBuilder(this, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                tvFinanceBankAdd.text = list[options1].split(" ")[0]
                bank = banks[options1]
                tvFinanceBankAdd.setTextColor(resources.getColor(R.color.colorBlack))
            }
        })
            .setContentTextSize(18)
            .setSelectOptions(1)
            .build<String>()

        view.setPicker(list)

        view.show()
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


    private fun showItemName() {

        val items = resources.getStringArray(R.array.finance_item)
        val eat = resources.getStringArray(R.array.finance_eat)
        val transportation = resources.getStringArray(R.array.finance_transportation)
        val shopping = resources.getStringArray(R.array.finance_shopping)
        val amusement = resources.getStringArray(R.array.finance_amusement)
        val medical = resources.getStringArray(R.array.finance_medical)
        val house = resources.getStringArray(R.array.finance_house)
        val investment = resources.getStringArray(R.array.finance_investment)
        val social = resources.getStringArray(R.array.finance_social)
        val business = resources.getStringArray(R.array.finance_business)
        val other = resources.getStringArray(R.array.finance_other)

        val subItem = mutableListOf<MutableList<String>>()

        subItem.add(transportation.toMutableList())
        subItem.add(shopping.toMutableList())
        subItem.add(amusement.toMutableList())
        subItem.add(eat.toMutableList())
        subItem.add(medical.toMutableList())
        subItem.add(house.toMutableList())
        subItem.add(investment.toMutableList())
        subItem.add(social.toMutableList())
        subItem.add(business.toMutableList())
        subItem.add(other.toMutableList())

        val view = OptionsPickerBuilder(this, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                tvFinanceItemAdd.text = items[options1] + "  " + subItem[options1][options2]
                tvFinanceItemAdd.setTextColor(resources.getColor(R.color.colorBlack))
            }
        })
            .setContentTextSize(18)
            .setSelectOptions(3, 1)
            .build<String>()

        view.setPicker(items.toMutableList(), subItem)

        view.show()
    }

    fun showIncomeDialog() {
        val incomes = resources.getStringArray(R.array.income).toMutableList()

        val view = OptionsPickerBuilder(this, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                tvFinanceItemAdd.text = incomes[options1]
                tvFinanceItemAdd.setTextColor(resources.getColor(R.color.colorBlack))
            }
        })
            .setContentTextSize(18)
            .build<String>()

        view.setPicker(incomes)

        view.show()
    }

    override fun initData() {
        toGetBankCards()
    }


    private fun toSaveFinance() {

        val type = tvFinanceItemAdd.text.toString()

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


        if (bank == null) {
            toast("请先添加用户")
            return
        }

        NetUtils.data(
            NetUtils.getApiInstance().saveFinance(
                BaseApplication.userId,
                type, time, account, remark, bank!!.name!!, bank!!.id!!, isExpense
            ),
            Consumer {

                toast("${it.message}")

                if (it.code == 200) {


                    EventBus.getDefault().post(FinanceAdd(it.data))

                    if (saveAgain) {
                        etFinanceAccountAdd.text = Editable.Factory.getInstance().newEditable("")
                        tvFinanceItemAdd.text = Editable.Factory.getInstance().newEditable("")
                        etFinanceRemarkAdd.text = Editable.Factory.getInstance().newEditable("")

                    } else {

                        finish()
                    }


                }
            }
        )
    }

    private fun toGetBankCards() {
        NetUtils.data(
            NetUtils.getApiInstance().getBank(BaseApplication.userId),
            Consumer {
                logInfo(it.toString())


                if (it.data.size > 0) {
                    banks.addAll(it.data)
                    bank = banks[0]
                    if (!update) {
                        tvFinanceBankAdd.text = bank.name
                    }
                }



                if (it.code == 200) {
                    it.data.forEachIndexed { index, bank ->
                        if (bank.type == "bank") {
                            list.add(bank.name + bank.number + " 余额" + decimalFormat.format(bank.account))
                        } else if (bank.type == "credit") {
                            list.add(bank.name + bank.number + " 可用余额" + decimalFormat.format(bank.amount - bank.debt))
                        } else {
                            list.add(bank.name + " 余额" + decimalFormat.format(bank.account))

                        }
                    }
                }

            }
        )
    }

}