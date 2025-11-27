package com.example.androidproject

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditCustomerActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etDateOfBirth: EditText
    private lateinit var cbIsActive: CheckBox
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_custormer)


        etEmail = findViewById(R.id.etEmail)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etPhoneNumber)
        etAddress = findViewById(R.id.etAddress)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        cbIsActive = findViewById(R.id.cbIsActive)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)


        userId = intent.getIntExtra("USER_ID", 0)
        if (userId == 0) {
            Toast.makeText(this, "User ID invalid!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadCustomerDetails()


        btnSave.setOnClickListener {
            updateCustomerDetails()
        }


        btnDelete.setOnClickListener {
            confirmAndDeleteCustomer()
        }
    }

    private fun updateCustomerDetails() {
        val email = etEmail.text.toString().trim()


        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show()
            return
        }


        val updatedCustomerRequest = UpdateCustomerRequest(
            email = email,

            first_name = etFirstName.text.toString().trim().takeIf { it.isNotEmpty() },
            last_name = etLastName.text.toString().trim().takeIf { it.isNotEmpty() },
            phone_number = etPhone.text.toString().trim().takeIf { it.isNotEmpty() },
            address = etAddress.text.toString().trim().takeIf { it.isNotEmpty() },
            date_of_birth = etDateOfBirth.text.toString().trim().takeIf { it.isNotEmpty() },

            is_active = cbIsActive.isChecked
        )


        RetrofitClient.instance.updateCustomer(userId, updatedCustomerRequest)
            .enqueue(object : Callback<NewCustomer> {
                override fun onResponse(call: Call<NewCustomer>, response: Response<NewCustomer>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditCustomerActivity, "Updated successfully!", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {

                        val errorMessage = try {
                            response.errorBody()?.string()?.substringAfter("error\":\"")?.substringBefore("\"}") ?: "Unknown Error"
                        } catch (e: Exception) {
                            "Unknown Error"
                        }
                        Toast.makeText(this@EditCustomerActivity, "Error ${response.code()}: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<NewCustomer>, t: Throwable) {
                    Toast.makeText(this@EditCustomerActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun confirmAndDeleteCustomer() {
        AlertDialog.Builder(this)
            .setTitle("Delete Customer")
            .setMessage("Are you sure you want to delete this customer?")
            .setPositiveButton("Yes") { _, _ ->
                RetrofitClient.instance.deleteCustomer(userId)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@EditCustomerActivity, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK)
                                finish()
                            } else {

                                Toast.makeText(this@EditCustomerActivity, "Error: Cannot delete customer (Code ${response.code()})", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(this@EditCustomerActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun loadCustomerDetails() {
        RetrofitClient.instance.getCustomer(userId)
            .enqueue(object : Callback<User1> {
                override fun onResponse(call: Call<User1>, response: Response<User1>) {
                    if (response.isSuccessful && response.body() != null) {
                        val customer = response.body()!!

                        etEmail.setText(customer.email)
                        etFirstName.setText(customer.first_name)
                        etLastName.setText(customer.last_name)
                        etPhone.setText(customer.phone_number ?: "")
                        etAddress.setText(customer.address ?: "")
                        etDateOfBirth.setText(customer.date_of_birth?.substringBefore("T") ?: "") // Cắt chuỗi ngày tháng
                        cbIsActive.isChecked = customer.is_active
                    } else {
                        Toast.makeText(this@EditCustomerActivity, "Error loading customer: ${response.code()}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<User1>, t: Throwable) {
                    Toast.makeText(this@EditCustomerActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }
}