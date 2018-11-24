package com.zhangwenshuan.dreamer.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper: SQLiteOpenHelper {

     constructor(context: Context) : super(context, DATABASE_NAME, null, DATABASE_VERSION)

    val passwordSql="create table ${PASSWORD_TABLE} (id Integer primary key,name varchar,username varchar,password varchar,type Integer,user_id Integer)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(passwordSql)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "dreamer.db"

        const val PASSWORD_TABLE="password"
    }
}

