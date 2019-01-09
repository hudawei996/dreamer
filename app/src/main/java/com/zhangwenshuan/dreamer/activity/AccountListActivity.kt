package com.zhangwenshuan.dreamer.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.AdapterView
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BankAccountAdapter
import com.zhangwenshuan.dreamer.bean.*
import com.zhangwenshuan.dreamer.util.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_account_list.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

class AccountListActivity : FinanceBaseActivity() {

    var list = mutableListOf<Bank>()


    lateinit var adapter: BankAccountAdapter


    override fun setResourceId(): Int {
        return R.layout.activity_account_list
    }

    override fun preInitData() {
        super.preInitData()
        EventBus.getDefault().register(this)

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

    var position = 0

    override fun initListener() {
        tvAdd.setOnClickListener {
            startActivity(Intent(this@AccountListActivity, AccountClassActivity::class.java))
        }

        lvBankAccount.setOnItemClickListener { parent, view, position, id ->

            this.position = position

            val bank = list[position]

            var intent: Intent

            if (bank.type == "credit") {
                intent = Intent(this@AccountListActivity, AccountCreditActivity::class.java)
            } else if (bank.type == "cash") {
                intent = Intent(this@AccountListActivity, AccountCashActivity::class.java)

            } else if (bank.type == "bank") {
                intent = Intent(this@AccountListActivity, AccountBankActivity::class.java)

            } else {
                intent = Intent(this@AccountListActivity, AccountMobileActivity::class.java)

            }


            val bundle = Bundle()

            bundle.putSerializable("bank", bank)

            intent.putExtra("data", bundle)

            startActivity(intent)
        }

        lvBankAccount.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                this@AccountListActivity.position = position

                var bank = list[position]

                if (bank.type == "cash") {
                    toast("现金账户不可删除")
                    return true
                }

                showDeleteDialog()

                return true
            }
        }
    }

    private fun showDeleteDialog() {
        var dialog = AlertDialog.Builder(this).setTitle("删除账户")
            .setMessage("删除该账户会删除所有相关收支")
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    toDelete()
                }
            }).setNegativeButton("取消", null).create()

        dialog.show()

    }

    private fun toDelete() {
        NetUtils.data(NetUtils.getApiInstance().deleteBank(list[position].id), Consumer {
            toast(it.message)
            if (it.code == 200) {
                list.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun initData() {
        toGetBankCards()
    }


    @Subscribe
    fun subscribeBank(add: BankAdd1) {
        list.add(0, add.bank)

        adapter.notifyDataSetChanged()
    }

    @Subscribe
    fun subscribeCash(add: CashAdd) {
        list.add(0, add.bank)

        adapter.notifyDataSetChanged()
    }

    @Subscribe
    fun subscribeCredit(add: CreditAdd) {
        list.add(0, add.bank)

        adapter.notifyDataSetChanged()
    }

    @Subscribe
    fun subscribeMobile(add: MobileAdd) {
        list.add(0, add.bank)

        adapter.notifyDataSetChanged()
    }

    @Subscribe
    fun subscribeUpdate(update: BankUpdate) {
        list[position] = update.bank

        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun toGetBankCards() {
        NetUtils.data(
            NetUtils.getApiInstance().getBank(BaseApplication.userId),
            Consumer {
                logInfo(it.toString())


                if (it.code == 200) {
                    list.addAll(it.data)
                    adapter.notifyDataSetChanged()
                }

            }
        )
    }



}