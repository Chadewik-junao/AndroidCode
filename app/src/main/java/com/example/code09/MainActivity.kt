package com.example.code09

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var titles: Array<String>
    private lateinit var authors: Array<String>
    private lateinit var newsList: List<New>

    private val NEWS_TITLE = "new_tltle"
    private val NEW_AUTHOR = "news_author"
    private val NEW_ID = "news_id"

//    private lateinit var dateList: ArrayList<Map<String, String>>

    //构建新闻列表
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //查询数据库
        val myDbHelper = MySQLiteOpenHelper(this)//使用次构造函数创建
        val db = myDbHelper.readableDatabase
        val cursor=db.query(NewsContract.NewsEntry().TABLE_NAME,null,null,null,null,null,null)

        //读取数据
        newsList = ArrayList<New>()
        val titleIndex=cursor.getColumnIndex(NewsContract.NewsEntry().COLUMN_NAME_TITLE)
        val authorIndex=cursor.getColumnIndex(NewsContract.NewsEntry().COLUMN_NAME_AUTHOR)
        val imageIndex=cursor.getColumnIndex(NewsContract.NewsEntry().COLUMN_NAME_IMAGE)
        while (cursor.moveToNext()){
            val title=cursor.getString(titleIndex)
            val author=cursor.getString(authorIndex)
            val image=cursor.getString(imageIndex)
            val bitmap=BitmapFactory.decodeStream(MainActivity::class.java.getResourceAsStream("/"+image))
            val news=New(title,author,bitmap)
            (newsList as ArrayList<New>).add(news)
        }

        val newAdapter: NewAdapter = NewAdapter(this, R.layout.list_item, newsList)
        val lvNewList: ListView = findViewById(R.id.lv_new_list)
        lvNewList.setAdapter(newAdapter)
        lvNewList.adapter = newAdapter
        lvNewList.setOnItemClickListener() { parent, view, position, id ->
            val new = newsList[position]
            Toast.makeText(this, new.mTile, Toast.LENGTH_SHORT).show()
        }


//        //直接使用Adapter构造
//        titles=resources.getStringArray(R.array.titles)
//        authors=resources.getStringArray(R.array.authors)
//
//        var adapter=ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,titles)
//        lv_new_list.adapter = adapter

//        //使用simpleadapter构造
//        initData()
//        var simpleAdapter=SimpleAdapter(this,dateList,android.R.layout.simple_list_item_2, arrayOf(NEWS_TITLE,NEW_AUTHOR), intArrayOf(android.R.id.text1,android.R.id.text2))
//        lv_new_list.adapter=simpleAdapter
//    }
//
//    fun initData(){
//        var length:Int
//        titles=resources.getStringArray(R.array.titles)
//        authors=resources.getStringArray(R.array.authors)
//        dateList= ArrayList<Map<String,String>>()
//        if(titles.size>authors.size)length=authors.size
//        else length=titles.size
//
//        for (i in 0 until length){
//            var map=HashMap<String,String>()
//            map.put(NEWS_TITLE,titles[i])
//            map.put(NEW_AUTHOR,authors[i])
//            dateList.add(map)
//
//        }


//        使用自定义的NewAdapter进行构造
//        initData()
//        val newAdapter: NewAdapter = NewAdapter(this, R.layout.list_item, newsList)
//        val lvNewList: ListView = findViewById(R.id.lv_new_list)
//        lvNewList.setAdapter(newAdapter)
//        lvNewList.adapter = newAdapter
//        lvNewList.setOnItemClickListener() { parent, view, position, id ->
//            val new = newsList[position]
//            Toast.makeText(this, new.mTile, Toast.LENGTH_SHORT).show()
//        }


    }

//    fun initData() {
//        //初始化数据，将数据存入内存
//        //数据量过多容易OOM
//
//        var length: Int
//        titles = resources.getStringArray(R.array.titles)
//        authors = resources.getStringArray(R.array.authors)
//        val image = resources.obtainTypedArray(R.array.images)
//        if (titles.size > authors.size) length = authors.size
//        else length = titles.size
//        newsList = ArrayList<New>()
//
//        for (i in 0 until length) {
//            val new = New(titles[i], authors[i], image.getResourceId(i, 0))
//            (newsList as ArrayList<New>).add(new)
//        }
//    }
}