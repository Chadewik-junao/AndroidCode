package com.example.code04

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //不同activity之间使用intent信息传递
        button2.setOnClickListener{
            Log.d("messge",message.getText().toString())
            Toast.makeText(this,message.getText().toString(),Toast.LENGTH_SHORT).show()
            val intent= Intent(this,MessageActivity::class.java)
            intent.putExtra("message",message.getText().toString())
            startActivity(intent)
        }
    }
}