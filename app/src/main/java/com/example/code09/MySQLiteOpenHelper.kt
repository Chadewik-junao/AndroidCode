package com.example.code09

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID


class MySQLiteOpenHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    //数据库创建和管理

    //定义建表语句
    private val SQL_CREATE_ENTRIES = "CREATE TABLE " + NewsContract.NewsEntry().TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY, " +
            NewsContract.NewsEntry().COLUMN_NAME_TITLE + " VARCHAR(200), " +
            NewsContract.NewsEntry().COLUMN_NAME_AUTHOR + " VARCHAR(100), " +
            NewsContract.NewsEntry().COLUMN_NAME_CONTENT + " TEXT, " +
            NewsContract.NewsEntry().COLUMN_NAME_IMAGE + " VARCHAR(100) " + ")"

    //定义删除表语句
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NewsContract.NewsEntry().TABLE_NAME

    public val DATABASE_VERSION = 1
    public val DATABASE_NAME = "news.db"

    //懒加载上下文
    private lateinit var mContext: Context

    //次构造函数，初始化上下文
    constructor(context: Context) : this(context, "news.db",1) {
        mContext = context
    }

    override fun onCreate(db: SQLiteDatabase) {
//        TODO("Not yet implemented")
        //自动生成todo在kotlin里面会报错

        db.execSQL(SQL_CREATE_ENTRIES)
        initDb(db)
    }

    private fun initDb(db: SQLiteDatabase?) {
        //初始化数据库，插入数据

        val titles=mContext.resources.getStringArray(R.array.titles)
        val authors=mContext.resources.getStringArray(R.array.authors)
//        val contents=mContext.resources.getStringArray(R.array.contents)
        val image=mContext.resources.obtainTypedArray(R.array.images)

        var length=0
        length=Math.min(titles.size,authors.size)
        length=Math.min(length,image.length())
        for(i in 0 until length){
            val value= ContentValues()
            value.put(NewsContract.NewsEntry().COLUMN_NAME_TITLE,titles[i])
            value.put(NewsContract.NewsEntry().COLUMN_NAME_AUTHOR,authors[i])
//            value.put(NewsContract.NewsEntry().COLUMN_NAME_CONTENT,contents[i])
            value.put(NewsContract.NewsEntry().COLUMN_NAME_IMAGE,image.getString(i))

            val r :Long?=db?.insert(NewsContract.NewsEntry().TABLE_NAME,null,value)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //删除数据，重新创建

//        TODO("Not yet implemented")
        //自动生成todo在kotlin里面会报错，语法错误，错误参照https://blog.csdn.net/github_34402358/article/details/107718802

        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }


}