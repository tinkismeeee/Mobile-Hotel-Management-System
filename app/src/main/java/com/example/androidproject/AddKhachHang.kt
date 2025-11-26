package com.example.androidproject

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddKhachHangActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etDob: EditText
    private lateinit var spGender: Spinner
    private lateinit var btnSaveCustomer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_khach_hang)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etPhoneNumber)
        etAddress = findViewById(R.id.etAddress)
        etDob = findViewById(R.id.etDateOfBirth)
        spGender = findViewById(R.id.spGender)
        btnSaveCustomer = findViewById(R.id.btnSaveCustomer)

        // Spinner
        val genderList = listOf("Nam", "Nữ")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGender.adapter = adapter

        // Save button click
        btnSaveCustomer.setOnClickListener {

            val customer = NewCustomer(
                username = etUsername.text.toString().trim(),
                password = etPassword.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                first_name = etFirstName.text.toString().trim(),
                last_name = etLastName.text.toString().trim(),
                phone_number = etPhone.text.toString().trim().ifEmpty { null },
                address = etAddress.text.toString().trim().ifEmpty { null },
                date_of_birth = etDob.text.toString().trim().ifEmpty { null },
                is_active = true
            )

            RetrofitClient.instance.addCustomer(customer)
                .enqueue(object : Callback<NewCustomer> {
                    override fun onResponse(
                        call: Call<NewCustomer>,
                        response: Response<NewCustomer>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AddKhachHangActivity,
                                "Thêm khách hàng thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddKhachHangActivity,
                                "Error: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<NewCustomer>, t: Throwable) {
                        Toast.makeText(
                            this@AddKhachHangActivity,
                            "Failed: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
