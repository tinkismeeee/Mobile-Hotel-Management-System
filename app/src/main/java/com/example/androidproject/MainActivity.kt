package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val email_input : EditText = findViewById<EditText>(R.id.email_input)
        val password_input : EditText = findViewById<EditText>(R.id.password_input)
        val signinBtn : Button = findViewById<Button>(R.id.signinBtn)
        val signupBtn : TextView = findViewById<TextView>(R.id.signupBtn)
        val incorrect_notification : TextView = findViewById<TextView>(R.id.incorrect_notification)
        val rememberBtn : RadioButton = findViewById<RadioButton>(R.id.rememberBtn)

        email_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                incorrect_notification.visibility = View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {
            override fun getTransformation(source: CharSequence, view: View): CharSequence {
                return PasswordCharSequence(source)
            }
            private inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {
                override fun get(index: Int): Char {
                    return '*' // This is the important part
                }
                override val length: Int
                    get() = source.length // Return default
                override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                    return source.subSequence(startIndex, endIndex) // Return default
                }
            }
        }
        password_input.transformationMethod = AsteriskPasswordTransformationMethod()

        password_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                incorrect_notification.visibility = View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        signinBtn.setOnClickListener {
            val email_inputStr : String? = email_input.text.toString()
            val password_inputStr : String? = password_input.text.toString()

            if (email_inputStr == "admin" && password_inputStr == "admin") {
                val intent = Intent(this, enterOTP::class.java)
                Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
                startActivity(intent)
            }
            else if (email_inputStr != "admin" || password_inputStr != "admin") {
                incorrect_notification.visibility = View.VISIBLE
            }
        }

        signupBtn.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
            startActivity(intent)
        }
    }

    fun Reset_all (email_input: EditText, password_input: EditText, incorrect_notification: TextView, rememberBtn: RadioButton) {
        email_input.text.clear()
        password_input.text.clear()
        incorrect_notification.visibility = View.GONE
        rememberBtn.isChecked = false
    }
}