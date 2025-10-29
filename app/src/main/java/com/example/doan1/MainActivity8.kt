package com.example.doan1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast

class MainActivity8 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main8)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val visaCheckbox = findViewById<CheckBox>(R.id.visa_default_checkbox)
        val mastercardCheckbox = findViewById<CheckBox>(R.id.mastercard_default_checkbox)


        visaCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                mastercardCheckbox.isChecked = false // Bỏ chọn thẻ còn lại
                Toast.makeText(this, "VISA set as default", Toast.LENGTH_SHORT).show()

            } else if (!mastercardCheckbox.isChecked) {

                visaCheckbox.isChecked = true
            }
        }


        mastercardCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                visaCheckbox.isChecked = false // Bỏ chọn thẻ còn lại
                Toast.makeText(this, "Mastercard set as default", Toast.LENGTH_SHORT).show()

            } else if (!visaCheckbox.isChecked) {

                mastercardCheckbox.isChecked = true
            }
        }
    }
}