package com.example.androidproject

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.isEmpty
import kotlin.text.trim

class AddLoaiphong : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_loaiphong)

        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tạo object NewRoomType
            val newRoomType = NewRoomType(name, description, true)

            // Gọi API thêm RoomType
            RetrofitClient.instance.addRoomType(newRoomType)
                .enqueue(object : Callback<RoomType> {
                    override fun onResponse(call: Call<RoomType>, response: Response<RoomType>) {
                        if (response.isSuccessful) {
                            // Lấy RoomType vừa thêm từ response
                            val addedRoom = response.body()
                            Toast.makeText(
                                this@AddLoaiphong,
                                "Thêm loại phòng thành công! (Active = ${addedRoom?.is_active})",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddLoaiphong,
                                "Lỗi: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RoomType>, t: Throwable) {
                        Toast.makeText(
                            this@AddLoaiphong,
                            "Thất bại: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
