package com.example.code08

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
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

        val spFileName = resources.getString(R.string.shared_preferences_file_name);
        val accountKey = resources.getString(R.string.login_account_name);
        val passwordKey = resources.getString(R.string.login_password);
        val rememberPasswordKey = resources.getString(R.string.login_remember_password);
        val spFile = getSharedPreferences(spFileName, MODE_PRIVATE)
        val account=spFile.getString(accountKey,null)
        val password=spFile.getString(passwordKey,null)
        val rememberPassword=spFile.getBoolean(rememberPasswordKey,false)

        //恢复密码
        if (accountKey!=null&& !TextUtils.isEmpty(accountKey))userinput.setText(account)
        if (passwordKey!=null&&!TextUtils.isEmpty(passwordKey))pwdinput.setText(password)
        cb_remember_pwd.isChecked = rememberPassword

        //登录按钮
        loginbutton.setOnClickListener {
            edituser = userinput?.getText().toString()
            editpwd = pwdinput?.getText().toString()
            //Toast.makeText(this, edituser + editpwd, Toast.LENGTH_SHORT).show()

            //记住密码逻辑
            var spFileName = resources.getString(R.string.shared_preferences_file_name)
            var accountKey = resources.getString(R.string.login_account_name)
            var passwordKey = resources.getString(R.string.login_password)
            var rememberPasswordKey = resources.getString(R.string.login_remember_password)
            val spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE)
            var editor = spFile.edit()
            if (cb_remember_pwd.isChecked) {
//                var password=pwdinput.text.toString()
//                var account=userinput.text.toString()
                Toast.makeText(this, userinput.text.toString()+pwdinput.text.toString()+rememberPasswordKey,Toast.LENGTH_SHORT).show()
                editor.putString(accountKey, userinput.text.toString())
                editor.putString(passwordKey, pwdinput.text.toString())
                editor.putBoolean(rememberPasswordKey, true)
                editor.apply()
            } else {
                editor.remove(accountKey)
                editor.remove(passwordKey)
                editor.remove(rememberPasswordKey)
                editor.apply()
            }
            startActivity(Intent(this, ShowNewsActivity::class.java))
        }

        //显示/隐藏密码逻辑
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