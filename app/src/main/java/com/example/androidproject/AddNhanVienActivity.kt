package com.example.androidproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNhanVienActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nhan_vien)

        // ÁNH XẠ THEO ĐÚNG THỨ TỰ JSON
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        btnSave = findViewById(R.id.btnSaveEmployee)

        btnSave.setOnClickListener {

            // TẠO BODY THEO ĐÚNG THỨ TỰ BACKEND MUỐN
            val newUser = NewUser(
                username = etUsername.text.toString().trim(),
                password = etPassword.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                first_name = etFirstName.text.toString().trim(),
                last_name = etLastName.text.toString().trim(),
                phone_number = etPhoneNumber.text.toString().trim()
            )

            RetrofitClient.instance.addStaff(newUser)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AddNhanVienActivity,
                                "Thêm nhân viên thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddNhanVienActivity,
                                "Error: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            this@AddNhanVienActivity,
                            "Failed: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}