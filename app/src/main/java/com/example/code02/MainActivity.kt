package com.example.code02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    var num=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_num.setText(R.string.text_num)
        //数据保存计数器
        //翻转界面
        if(savedInstanceState!=null){
            num=savedInstanceState.getInt("num_key")
            text_num.setText(savedInstanceState.getInt("num_key").toString())
        }
        button.setOnClickListener{
            num++
            text_num.setText(num.toString())
        }
        button2.setOnClickListener{
            num--
            text_num.setText(num.toString())
        }
    }

    //暂存数据并传递
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("num_key",num)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Tag","on Destroy")
    }
}