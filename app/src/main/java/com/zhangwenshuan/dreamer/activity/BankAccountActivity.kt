package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankAccountAdapter
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_bank_account.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BankAccountActivity : FinanceBaseActivity() {

    var list = mutableListOf<BankCard>()


    lateinit var adapter: BankAccountAdapter


    override fun setResourceId(): Int {
        return R.layout.activity_bank_account
    }

    override fun preInitData() {
        super.preInitData()
        EventBus.getDefault().register(this)

        val data = getBankCarsFromLocal()

        if (data != null) {
            list.addAll(data)
        }

        adapter = BankAccountAdapter(this, list!!)

        lvBankAccount.adapter = adapter

        tvTitle.text = "账户"

    }

    override fun initViews() {

        tvAdd.typeface = Typeface.createFromAsset(assets, "icon_action.ttf")

        tvAdd.text = resources.getString(R.string.add)

        tvAdd.visibility = View.VISIBLE

        tvAdd.textSize = 20f
    }

    private var position = 0

    override fun initListener() {
        tvAdd.setOnClickListener {
            startActivity(Intent(this@BankAccountActivity, BankCardAddActivity::class.java))
        }

        lvBankAccount.setOnItemClickListener { parent, view, position, id ->

            this.position = position

            val intent = Intent(this@BankAccountActivity, BankCardAddActivity::class.java)

            val bundle = Bundle()

            bundle.putSerializable("bank", list[position])

            intent.putExtra("data", bundle)

            startActivity(intent)
        }
    }

    override fun initData() {
    }

    @Subscribe
    fun subscribe(update: BankUpdate) {
        list[position] = update.bank
        adapter.notifyDataSetChanged()
    }

    @Subscribe
    fun subscribeDelte(delete: BankDelete) {
        list.removeAt(position)

        adapter.notifyDataSetChanged()
    }

    @Subscribe
    fun subscribeDelte(add: BankAdd) {
        list.add(0, add.bank)

        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}