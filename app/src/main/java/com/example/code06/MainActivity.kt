package com.example.code06

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var pwdswitch = false
//    private lateinit var edituser: String
//    private lateinit var editpwd: String
    private var edituser: String = ""
    private var editpwd: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //登录按钮
        loginbutton.setOnClickListener {
            edituser = userinput?.getText().toString()
            editpwd = pwdinput?.getText().toString()
            Toast.makeText(this,edituser+editpwd,Toast.LENGTH_SHORT).show()
        }

        //显示、隐藏密码
        pwd_switch.setOnClickListener {
            pwdswitch = !pwdswitch
            if (pwdswitch) {
                pwd_switch.setImageResource(R.drawable.ic_visibility)
                pwdinput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            } else {
                pwd_switch.setImageResource(R.drawable.ic_visibility_off)
                pwdinput.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            }

        }
    }
}