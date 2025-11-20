package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast

class card_profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.card_profile)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
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