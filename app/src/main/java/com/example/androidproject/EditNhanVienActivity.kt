package com.example.androidproject

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNhanVienActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var cbIsActive: CheckBox
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nhan_vien)

        // Ánh xạ view
        etEmail = findViewById(R.id.etEmail)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etPhoneNumber)
        cbIsActive = findViewById(R.id.cbIsActive)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        // Lấy userId từ Intent
        userId = intent.getIntExtra("USER_ID", 0)
        if (userId == 0) {
            Toast.makeText(this, "User ID invalid!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadUserDetails()

        // Nút Save
        btnSave.setOnClickListener {
            val updatedUser = UpdateUserRequest(
                email = etEmail.text.toString().trim(),
                first_name = etFirstName.text.toString().trim(),
                last_name = etLastName.text.toString().trim(),
                phone_number = etPhone.text.toString().trim(),
                is_active = cbIsActive.isChecked
            )

            RetrofitClient.instance.updateStaff(userId, updatedUser)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@EditNhanVienActivity,
                                "Updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(
                                this@EditNhanVienActivity,
                                "Error: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            this@EditNhanVienActivity,
                            "Failed: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }


        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes") { _, _ ->
                    RetrofitClient.instance.deleteStaff(userId)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        this@EditNhanVienActivity,
                                        "Deleted successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    setResult(RESULT_OK)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@EditNhanVienActivity,
                                        "Error: ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(
                                    this@EditNhanVienActivity,
                                    "Failed: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun loadUserDetails() {
        RetrofitClient.instance.getUser(userId)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()!!
                        etEmail.setText(user.email)
                        etFirstName.setText(user.first_name)
                        etLastName.setText(user.last_name)
                        etPhone.setText(user.phone_number)
                        cbIsActive.isChecked = user.is_active
                    } else {
                        Toast.makeText(
                            this@EditNhanVienActivity,
                            "Error loading user: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(
                        this@EditNhanVienActivity,
                        "Failed: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
