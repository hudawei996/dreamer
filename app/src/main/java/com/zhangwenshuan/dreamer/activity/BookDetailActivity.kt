package com.zhangwenshuan.dreamer.activity

import android.text.Editable
import android.view.View
import android.widget.EditText
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.Book
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.jetbrains.anko.toast

class BookDetailActivity : BaseActivity() {

    lateinit var book: Book

    override fun setResourceId(): Int {
        return R.layout.activity_book_detail
    }

    override fun preInitData() {
        book = intent.getBundleExtra("data").getSerializable("book") as Book
    }

    override fun initViews() {

        tvAdd.visibility= View.VISIBLE

        tvAdd.text="更新"

        tvTitle.text = book.name

        tvBookName.text=book.name

        if (book.end=="在读"){
            tvTime.text=book.end
            tvTime.setTextColor(resources.getColor(R.color.colorPrimary))
        }else{
            tvTime.text="${book.begin} 至 ${book.end}"
        }

        if (book.content.isEmpty()){
            etBookContent.hint="还没有写内容哦"
        }

        if (book.evaluate.isEmpty()){
            etBookEvaluate.hint="还没有收获内容哦"
        }

        etBookContent.text=Editable.Factory.getInstance().newEditable(book.content)

        etBookEvaluate.text=Editable.Factory.getInstance().newEditable(book.evaluate)
    }

    override fun initListener() {
        tvAdd.setOnClickListener {
            toUpdate()
        }

    }

    private fun toUpdate() {
        NetUtils.data(NetUtils.getApiInstance().updateBook(book.id!!,etBookContent.text.toString(),etBookEvaluate.text.toString()),
            Consumer {
                if (it.code==200){
                    toast("更新成功")
                    finish()
                }else{
                    toast("更新失败")

                }
            })
    }

    override fun initData() {

    }
}