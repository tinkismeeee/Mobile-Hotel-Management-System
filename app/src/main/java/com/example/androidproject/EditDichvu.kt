package com.example.androidproject

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.trim

class EditDichvuActivity : AppCompatActivity() {

    private lateinit var etServiceCode: EditText
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var cbAvailability: CheckBox
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var serviceId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_dichvu)

        // Ánh xạ view
        etServiceCode = findViewById(R.id.etService_code)
        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        cbAvailability = findViewById(R.id.cbAvailability)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        // Lấy serviceId từ Intent
        serviceId = intent.getIntExtra("SERVICE_ID", 0)
        if (serviceId == 0) {
            Toast.makeText(this, "Service ID invalid!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Nút Save: gộp luôn logic update
        btnSave.setOnClickListener {
            val updatedService = UpdateServiceRequest(
                service_code = etServiceCode.text.toString().trim(),
                name = etName.text.toString().trim(),
                price = etPrice.text.toString().trim(),
                availability = cbAvailability.isChecked,
                description = etDescription.text.toString().trim()
            )

            RetrofitClient.instance.updateService(serviceId, updatedService)
                .enqueue(object : Callback<Service> {
                    override fun onResponse(call: Call<Service>, response: Response<Service>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditDichvuActivity, "Service updated successfully!", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this@EditDichvuActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Service>, t: Throwable) {
                        Toast.makeText(this@EditDichvuActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // Nút Delete: gộp luôn logic xóa
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Service")
                .setMessage("Are you sure you want to delete this service?")
                .setPositiveButton("Yes") { _, _ ->
                    RetrofitClient.instance.deleteService(serviceId)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@EditDichvuActivity, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                                    setResult(RESULT_OK)
                                    finish()
                                } else {
                                    Toast.makeText(this@EditDichvuActivity, "Cannot delete service: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(this@EditDichvuActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                .setNegativeButton("No", null)
                .show()
        }

        // Load service ở cuối onCreate
        loadServiceDetails()
    }

    // Chỉ giữ lại hàm loadServiceDetails
    private fun loadServiceDetails() {
        RetrofitClient.instance.getService(serviceId)
            .enqueue(object : Callback<Service> {
                override fun onResponse(call: Call<Service>, response: Response<Service>) {
                    if (response.isSuccessful && response.body() != null) {
                        val service = response.body()!!
                        etServiceCode.setText(service.service_code)
                        etName.setText(service.name)
                        etPrice.setText(service.price)
                        cbAvailability.isChecked = service.availability
                        etDescription.setText(service.description)
                    } else {
                        Toast.makeText(this@EditDichvuActivity, "Error loading service: ${response.code()}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<Service>, t: Throwable) {
                    Toast.makeText(this@EditDichvuActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }
}
