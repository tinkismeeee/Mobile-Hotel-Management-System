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
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.io.File

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
        // Write a message to the database
//        val database = Firebase.database
//        val myRef = database.getReference("message2")
//        myRef.setValue("Hello, World!")
        checkUserSession()
        val email_input : EditText = findViewById<EditText>(R.id.email_input)
        val password_input : EditText = findViewById<EditText>(R.id.password_input)
        val signinBtn : Button = findViewById<Button>(R.id.signinBtn)
        val signupBtn : TextView = findViewById<TextView>(R.id.signupBtn)
        val incorrect_notification : TextView = findViewById<TextView>(R.id.incorrect_notification)
        val rememberBtn : RadioButton = findViewById<RadioButton>(R.id.rememberBtn)
        val password_visibility = findViewById<ImageView>(R.id.password_visibility)

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
                val masterKey = MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
                val session = EncryptedSharedPreferences.create(
                    this,
                    getString(R.string.session_file_name),
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
                val mySession = session.edit()
                mySession.putString("email", email_inputStr)
                mySession.putString("password", password_inputStr)
                mySession.putBoolean("isLoggedIn", true)
                mySession.apply()

                val intent = Intent(this, Home::class.java)
                Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
                startActivity(intent)
                finish()
            }
            else if (email_inputStr != "admin" || password_inputStr != "admin") {
                incorrect_notification.visibility = View.VISIBLE
            }
        }

        signupBtn.setOnClickListener {
            val intent : Intent = Intent(this, signup::class.java)
            Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
            startActivity(intent)
        }

        password_visibility.setOnClickListener {
            if (password_input.transformationMethod == null) {
                password_input.transformationMethod = AsteriskPasswordTransformationMethod()
            }
            else {
                password_input.transformationMethod = null
            }
        }
    }

    private fun Reset_all (email_input: EditText, password_input: EditText, incorrect_notification: TextView, rememberBtn: RadioButton) {
        email_input.text.clear()
        password_input.text.clear()
        incorrect_notification.visibility = View.GONE
        rememberBtn.isChecked = false
    }

    private fun checkUserSession() {
        // Login Session
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val session = EncryptedSharedPreferences.create(
            this,
            getString(R.string.session_file_name),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        val isLoggedIn = session.getBoolean("isLoggedIn", false)
        if (isLoggedIn == true) {
            Log.i("Login Session", "User is logged in")
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
        else {
            Log.e("Login Session", "User is not logged in")
        }
    }
}