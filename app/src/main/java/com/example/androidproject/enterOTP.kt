package com.example.androidproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


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

        continueBtn.setOnClickListener {
            val first_numberStr = first_number.text.toString()
            val second_numberStr = second_number.text.toString()
            val third_numberStr = third_number.text.toString()
            val fourth_numberStr = fourth_number.text.toString()

            if (first_numberStr != "" && second_numberStr != "" && third_numberStr != "" && fourth_numberStr != "") {
                disable_all(first_number, second_number, third_number, fourth_number, continueBtn)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                blurLayout.visibility = View.VISIBLE
                terms_layout.visibility = View.VISIBLE
                terms_layout.bringToFront()
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
}