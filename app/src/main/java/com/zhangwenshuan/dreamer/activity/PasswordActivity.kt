package com.zhangwenshuan.dreamer.activity

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.LogoType
import com.zhangwenshuan.dreamer.bean.Password
import com.zhangwenshuan.dreamer.database.DatabaseHelper
import com.zhangwenshuan.dreamer.fragment.PasswordListFragment
import com.zhangwenshuan.dreamer.util.userId
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : BaseActivity() {

    lateinit var db: DatabaseHelper

    lateinit var writer: SQLiteDatabase

    lateinit var reader: SQLiteDatabase


    override fun setResourceId(): Int {
        return R.layout.activity_password
    }

    override fun preInitData() {
        initDatabase()
    }

    private fun initDatabase() {
        db = DatabaseHelper(this)

        writer = db.writableDatabase

        reader = db.readableDatabase
    }

    override fun initViews() {
        findViewById<View>(android.support.v7.appcompat.R.id.search_plate).setBackground(null)
        findViewById<View>(android.support.v7.appcompat.R.id.submit_area).setBackground(null)

        passwordListFragment = PasswordListFragment()
        supportFragmentManager.beginTransaction().add(R.id.flPasswordContent, passwordListFragment!!).commit()
    }

    override fun initListener() {
        ivAdd.setOnClickListener {
            startActivity(Intent(this@PasswordActivity, PasswordAddActivity::class.java))
        }


        svPassword.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {

                Log.e("dreamer", "close")

                return true
            }
        })




        svPassword.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                toSearchPassword(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length == 0) {
                    passwordListFragment.showAllPassword()

                    closeSoftInput()
                }
                return false
            }

        })
    }

    private fun toSearchPassword(passwordName: String?) {

        val cursor = reader.rawQuery(
            "select * from ${DatabaseHelper.PASSWORD_TABLE} where name like '${passwordName}%' ",
            null,
            null
        )

        val list = cursorToPassword(cursor)

        passwordListFragment.searchPassword(list)

        closeSoftInput()

    }

    lateinit var passwordListFragment: PasswordListFragment

    override fun initData() {

    }

    fun closeSoftInput() {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        manager.hideSoftInputFromWindow(svPassword.windowToken,0)
    }
}

fun cursorToPassword(cursor: Cursor): MutableList<Password> {
    var list: MutableList<Password> = mutableListOf()

    val types=LogoType.values()

    if (cursor != null) {

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val username = cursor.getString(cursor.getColumnIndex("username"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val userId = cursor.getInt(cursor.getColumnIndex("user_id"))
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val type=cursor.getInt(cursor.getColumnIndex("type"))

            list.add(Password(name, username, password, userId,types[type],id))

        }

        cursor.close()
    }

    return list
}
