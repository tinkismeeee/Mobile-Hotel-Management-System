package com.example.androidproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.File
import okhttp3.*
import java.io.IOException

class enterOTP : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enter_otp)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val blurLayout = findViewById<io.alterac.blurkit.BlurLayout>(R.id.blurLayout)
        val first_number = findViewById<EditText>(R.id.first_number)
        val second_number = findViewById<EditText>(R.id.second_number)
        val third_number = findViewById<EditText>(R.id.third_number)
        val fourth_number = findViewById<EditText>(R.id.fourth_number)
        val continueBtn = findViewById<Button>(R.id.continueBtn)
        val terms_layout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.terms_layout)
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        val agreeBtn = findViewById<Button>(R.id.agreeBtn)
        val disagreeBtn = findViewById<Button>(R.id.disagreeBtn)
        val email_holder = findViewById<TextView>(R.id.email_holder)
        var OTP_code : String = ""
        var otp_check : Boolean = false

        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val session = EncryptedSharedPreferences.create(
            this,
            "mySession",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val email = session.getString("email", null)
        email_holder.setText(email)
        Log.i("email", email.toString())
        sendOtp(email.toString()) { success, message ->
            runOnUiThread {
                Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                if (success) {
                    Log.i("OTP", "OTP sent successfully")
                }
            }
        }

        continueBtn.setOnClickListener {
            val first_numberStr = first_number.text.toString()
            val second_numberStr = second_number.text.toString()
            val third_numberStr = third_number.text.toString()
            val fourth_numberStr = fourth_number.text.toString()
            val client = OkHttpClient()
            if (first_numberStr.isEmpty() || second_numberStr.isEmpty() || third_numberStr.isEmpty() || fourth_numberStr.isEmpty()) {
                Toast.makeText(this, "Please enter the valid OTP code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (first_numberStr != "" && second_numberStr != "" && third_numberStr != "" && fourth_numberStr != "") {
                OTP_code = first_numberStr + second_numberStr + third_numberStr + fourth_numberStr
                verifyOtp(email.toString(), OTP_code) { success, message ->
                    otp_check = success
                    runOnUiThread {
                        if (otp_check) {
                            Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show()
                            Log.d("OTP", "OTP is correct")
                        }
                        else {
                            Toast.makeText(this, "OTP is incorrect. Please try again", Toast.LENGTH_LONG).show()
                            Log.e("OTP", "OTP is incorrect")
                            first_number.text.clear()
                            second_number.text.clear()
                            third_number.text.clear()
                            fourth_number.text.clear()
                            first_number.requestFocus()
                        }
                    }
                }
            }
            else {
                Toast.makeText(this, "Please enter the valid OTP code", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn.setOnClickListener {
            finish()
        }

        agreeBtn.setOnClickListener {

        }

        disagreeBtn.setOnClickListener {
            Toast.makeText(this, "You have declined the terms and conditions", Toast.LENGTH_SHORT).show();
            val resultIntent : Intent = Intent()
            setResult(RESULT_CANCELED, resultIntent)
            val file = File(this.filesDir.parent + "/shared_prefs/mySession.xml")
            if (file.exists()) {
                file.delete()
            }
            finish()
        }
    }

    fun disable_all (first_number: EditText, second_number: EditText, third_number: EditText, fourth_number: EditText, continueBtn: Button) {
        first_number.isEnabled = false
        second_number.isEnabled = false
        third_number.isEnabled = false
        fourth_number.isEnabled = false
        continueBtn.isEnabled = false
    }

    fun sendOtp(email: String, callback: (Boolean, String) -> Unit) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("email", email)
            .build()
        val request = Request.Builder()
            .url("http://192.168.1.125:3000/send-otp")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, "Failed to send OTP")
                Log.e("OTP", "Failed to send OTP")
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                callback(true, result.toString())
            }
        })
    }
    fun verifyOtp(email: String, otp: String, callback: (Boolean, String) -> Unit) {
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("email", email)
            .add("otp", otp)
            .build()
        val request = Request.Builder()
            .url("http://192.168.1.125:3000/verify-otp")
            .post(formBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message ?: "Failed to verify OTP")
            }
            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string() ?: ""
                val success = result.contains("success")
                callback(success, result)
            }
        })
    }
}