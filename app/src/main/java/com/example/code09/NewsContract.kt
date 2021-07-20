package com.example.code09

import android.provider.BaseColumns

class NewsContract {
    private fun NewsContract() {}
    public class NewsEntry : BaseColumns {
        //数据库合约类，内部类
        public val TABLE_NAME = "tbl_news"
        public val COLUMN_NAME_TITLE = "title"
        public val COLUMN_NAME_CONTENT = "content"
        public val COLUMN_NAME_AUTHOR = "author"
        public val COLUMN_NAME_IMAGE = "image"
    }
}