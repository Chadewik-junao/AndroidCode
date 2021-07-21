package com.example.ggnew

import android.accounts.NetworkErrorException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.Array
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    private lateinit var newsData: List<New>
    private lateinit var adapter: NewAdapter

    var page = 1
    var mCurrentColIndex = 4
    var mCols = arrayOf(
        Constant.NEWS_COL5,
        Constant.NEWS_COL7, Constant.NEWS_COL8,
        Constant.NEWS_COL10, Constant.NEWS_COL11
    )

    //利用匿名类实现接口并赋值变量
    private var callbak = object : okhttp3.Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("TAG", "Failed to connect server!")
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val body = response.body()!!.string()
                runOnUiThread {
                    kotlin.run {
                        val gson: Gson = Gson()

                        //利用匿名类实现接口并赋值变量
                        val jsonType = object : TypeToken<BaseResponse<List<New>>>() {}.type
                        val newsListResponse =
                            gson.fromJson(body, jsonType) as BaseResponse<List<New>>
                        for (new in newsListResponse.data!!) {
                            adapter.add(new)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        ininData()
    }

    private fun ininData() {
        newsData=ArrayList<New>()
        adapter= NewAdapter(this,R.layout.list_item,newsData)
        lv_news_list.adapter=adapter
        refresData(1)
    }

    private fun refresData(i: Int) {
        Thread(object : Runnable {
            override fun run() {
                val requestObj=NewsRequest()
                requestObj.col=mCols[mCurrentColIndex]
                requestObj.num=Constant.NEWS_NUM
                requestObj.page=page
                val urlParams = requestObj.toString()
                val request:Request=Request.Builder().url(Constant.IT_NEWS_URI+urlParams).get().build()
                try {
                    val client=OkHttpClient()
                    client.newCall(request).enqueue(callbak)
                }catch (ex:NetworkErrorException) {
                    ex.printStackTrace()
                }
            }
        }).start()
    }

    private fun initView() {
        lv_news_list.setOnItemClickListener { adapterView: AdapterView<*>?,
                                              view: View?, position: Int, l: Long ->
            var intent = Intent(this, DetailActivity::class.java)
            var news = adapter.getItem(position)
            intent.putExtra(Constant.IT_NEWS_URI, news?.mContentUrl)
            intent.putExtra("title",news?.mTitle)
            intent.putExtra("subtitle",news?.mPublishTime)
            intent.putExtra("text",news?.mSource)
            startActivity(intent)
        }
    }
}