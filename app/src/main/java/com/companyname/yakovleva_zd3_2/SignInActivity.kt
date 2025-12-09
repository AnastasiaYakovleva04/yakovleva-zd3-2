package com.companyname.yakovleva_zd3_2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class SignInActivity : Activity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)

        login.setOnClickListener(){
            if (email.text.isNullOrEmpty() || password.text.isNullOrEmpty()){
                val alert = AlertDialog.Builder(this)
                    .setTitle("Ошибка")
                    .setMessage("Все поля должны быть заполнены")
                    .setPositiveButton("Ок", null)
                    .create()
                alert.show()
            }
            else{
                val intent = Intent(this@SignInActivity, QuestsActivity::class.java)
                startActivity(intent)
            }
        }
    }

}