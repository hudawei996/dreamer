package com.zhangwenshuan.dreamer.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(var context: Context) : SQLiteOpenHelper(context, "dreamer", null, 1) {

    var dreamer =
        "create table dreamer(id INTEGER primary key autoincrement,target String,begin_time String,end_time String," +
                "created_time String,final INTEGER,user_id INTEGER,oder INTEGER default 0,show INTEGER)"

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(dreamer)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }




}