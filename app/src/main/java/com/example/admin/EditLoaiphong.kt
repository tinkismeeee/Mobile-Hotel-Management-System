package com.example.admin

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLoaiphong : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var roomTypeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_loaiphong)

        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        // Lấy roomTypeId từ Intent
        roomTypeId = intent.getIntExtra("ROOM_TYPE_ID", 0)
        if (roomTypeId == 0) {
            Toast.makeText(this, "Room Type ID invalid!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load thông tin loại phòng
        loadRoomTypeDetails()

        // Nút Save: cập nhật
        btnSave.setOnClickListener {
            val updatedRoomType = UpdateRoomTypeRequest(
                name = etName.text.toString().trim(),
                description = etDescription.text.toString().trim(),
                is_active = true // luôn true
            )

            RetrofitClient.instance.updateRoomType(roomTypeId, updatedRoomType)
                .enqueue(object : Callback<RoomType> {
                    override fun onResponse(call: Call<RoomType>, response: Response<RoomType>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditLoaiphong, "Updated successfully!", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this@EditLoaiphong, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RoomType>, t: Throwable) {
                        Toast.makeText(this@EditLoaiphong, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // Nút Delete
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Room Type")
                .setMessage("Are you sure you want to delete this room type?")
                .setPositiveButton("Yes") { _, _ ->
                    RetrofitClient.instance.deleteRoomType(roomTypeId)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@EditLoaiphong, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                } else {
                                    Toast.makeText(this@EditLoaiphong, "Cannot delete: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(this@EditLoaiphong, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun loadRoomTypeDetails() {
        RetrofitClient.instance.getRoomType(roomTypeId)
            .enqueue(object : Callback<RoomType> {
                override fun onResponse(call: Call<RoomType>, response: Response<RoomType>) {
                    if (response.isSuccessful && response.body() != null) {
                        val room = response.body()!!
                        etName.setText(room.name)
                        etDescription.setText(room.description)
                        // luôn true, không cần hiển thị CheckBox
                    } else {
                        Toast.makeText(this@EditLoaiphong, "Error loading room type: ${response.code()}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<RoomType>, t: Throwable) {
                    Toast.makeText(this@EditLoaiphong, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }
}
