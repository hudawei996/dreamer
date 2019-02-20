package com.zhangwenshuan.dreamer.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.adapter.BookListAdapter
import com.zhangwenshuan.dreamer.adapter.OnItemClickListener
import com.zhangwenshuan.dreamer.bean.Book
import com.zhangwenshuan.dreamer.bean.BookAdd
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import com.zhangwenshuan.dreamer.util.TimeUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_book_list.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import java.util.*

class BookListActivity : BaseActivity() {

    var list = mutableListOf<Book>()

    lateinit var adapter: BookListAdapter

    var curPosition = 0

    override fun setResourceId(): Int {
        return R.layout.activity_book_list
    }

    override fun preInitData() {
        EventBus.getDefault().register(this)

        adapter = BookListAdapter(this, list)

        lvBookList.adapter = adapter
    }

    override fun initViews() {
        tvTitle.text = "书架"
    }

    override fun initListener() {
        flbAdd.setOnClickListener { startActivity(Intent(this@BookListActivity, BookAddActivity::class.java)) }


        adapter.setBookNameClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                curPosition = position

                val book = list[position]

                val bundle = Bundle()
                bundle.putSerializable("book", book)
                val intent = Intent(this@BookListActivity, BookDetailActivity::class.java)
                intent.putExtra("data", bundle)

                startActivity(intent)
            }
        })

        adapter.setEndTimeClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                curPosition = position
                showDatePickerDialog(position)
            }
        })


        adapter.setBookNameLongClickLisenter(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                showDeleteDialog(position)
            }
        })
    }

    private fun showDeleteDialog(position: Int) {
        val dialog = AlertDialog.Builder(this).setMessage("确定删除该书")
            .setPositiveButton("确定",object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    toDelete(position)
                }
            })
            .create()

        dialog.show()
    }

    private fun toDelete(position: Int) {
        NetUtils.data(NetUtils.getApiInstance().deleteBook(list[position].id!!), Consumer {
            if (it.code==200){
                list.removeAt(position)
                adapter.notifyDataSetChanged()
                notifyViewChange()
            }

            toast(it.message)
        })
    }

    override fun initData() {
        toGetBook()
    }

    private fun showDatePickerDialog(position: Int) {

        val arrays = arrayOf(true, true, true, false, false, false)

        val pvTime = TimePickerBuilder(this@BookListActivity, object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                val end = TimeUtils.getDay(date!!)

                if (end < list[position].begin) {
                    toast("结束时间不应该小于开始时间")
                    return
                }

                toUpdateEnd(end)
            }
        })
            .setType(arrays.toBooleanArray())
            .setSubmitColor(resources.getColor(R.color.colorPrimary))//确定按钮文字颜色
            .setCancelColor(resources.getColor(R.color.colorPrimary))//取消按钮文字颜色
            .setTextColorCenter(resources.getColor(R.color.colorPrimary))
            .build()

        pvTime.show()

    }

    private fun toUpdateEnd(end: String) {
        NetUtils.data(NetUtils.getApiInstance().updateBook(list[curPosition].id!!, end), Consumer {
            if (it.code == 200) {
                list[curPosition].end = end
                adapter.notifyDataSetChanged()
            }
        })

    }


    private fun toGetBook() {
        NetUtils.data(NetUtils.getApiInstance().getBook(BaseApplication.userId), Consumer {
            if (it.code == 200) {
                list.clear()
                list.addAll(it.data)

                adapter.notifyDataSetChanged()

                notifyViewChange()
            }
        })
    }

    private fun notifyViewChange() {
        if (list.size == 0) {
            lvBookList.visibility = View.GONE
            tvNotBook.visibility = View.VISIBLE
        } else {
            lvBookList.visibility = View.VISIBLE
            tvNotBook.visibility = View.GONE

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subsribleBookAdd(book: BookAdd) {
        toGetBook()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}