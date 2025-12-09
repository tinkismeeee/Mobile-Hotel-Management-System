package com.example.androidproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class forgot_password : AppCompatActivity() {
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var email_input : EditText
    lateinit var continueBtn : Button
    lateinit var backBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email_input = findViewById<EditText>(R.id.email_input)
        val continueBtn = findViewById<Button>(R.id.continueBtn)
        val backBtn = findViewById<ImageView>(R.id.backBtn)


        continueBtn.setOnClickListener {
            val email_inputString = email_input.text.toString()
            if (email_inputString.isNotEmpty()) {
                sendPassResetEmail(email_inputString)
            }
            else {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
            }
            email_input.text.clear()
        }

        backBtn.setOnClickListener {
            finish()
        }

    }

    fun sendPassResetEmail (emailString: String) {
        mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}