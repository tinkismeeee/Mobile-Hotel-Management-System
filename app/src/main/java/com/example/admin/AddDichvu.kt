package com.example.admin

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDichvu : AppCompatActivity() {

    private lateinit var etServiceCode: EditText
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var spAvailability: Spinner
    private lateinit var etDescription: EditText
    private lateinit var btnSaveService: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dichvu)

        // Bind view
        etServiceCode = findViewById(R.id.etServiceCode)
        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        spAvailability = findViewById(R.id.spAvailability)
        etDescription = findViewById(R.id.etDescription)
        btnSaveService = findViewById(R.id.btnSaveService)

        // Spinner Availability (True/False)
        val availabilityList = listOf("Available", "Not Available")
        val spAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availabilityList)
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spAvailability.adapter = spAdapter

        // Save button click
        btnSaveService.setOnClickListener {
            val isAvailable = spAvailability.selectedItem.toString() == "Available"

            val service = NewService(
                service_code = etServiceCode.text.toString().trim(),
                name = etName.text.toString().trim(),
                price = etPrice.text.toString().trim(),
                availability = isAvailable,
                description = etDescription.text.toString().trim()
            )

            // Gọi API thêm dịch vụ
            RetrofitClient.instance.addService(service)
                .enqueue(object : Callback<NewService> {
                    override fun onResponse(call: Call<NewService>, response: Response<NewService>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddDichvu, "Thêm dịch vụ thành công!", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this@AddDichvu, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<NewService>, t: Throwable) {
                        Toast.makeText(this@AddDichvu, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
