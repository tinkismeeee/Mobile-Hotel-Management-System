package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)

        // Ánh xạ các thành phần UI từ layout
        val btnManageEmployees: Button = findViewById(R.id.btnManageEmployees)
        val btnManageCustomers: Button = findViewById(R.id.btnManageCustomers)
        val btnManageServices: Button = findViewById(R.id.btnManageServices)
        val btnRevenue: Button = findViewById(R.id.btnRevenue)
        val btnLogout: Button = findViewById(R.id.btnLogout) // Ánh xạ nút Logout mới

        val navHome: LinearLayout = findViewById(R.id.navHome)

        // Thiết lập OnClickListener cho các nút chức năng
        btnManageEmployees.setOnClickListener {
            Toast.makeText(this, "Bạn đã nhấn Quản lý nhân viên", Toast.LENGTH_SHORT).show()
            // Thêm code để mở màn hình Quản lý nhân viên tại đây
        }

        btnManageEmployees.setOnClickListener {
            val intent = Intent(this, NhanVienActivity::class.java)
            startActivity(intent)
        }
        btnManageCustomers.setOnClickListener {
            Toast.makeText(this, "Bạn đã nhấn Quản lý khách hàng", Toast.LENGTH_SHORT).show()
            // Thêm code để mở màn hình Quản lý khách hàng tại đây
        }
        btnManageCustomers.setOnClickListener {
            val intent = Intent(this, KhachHangActivity::class.java)
            startActivity(intent)
        }
        btnManageServices.setOnClickListener {
            Toast.makeText(this, "Bạn đã nhấn Quản lý dịch vụ", Toast.LENGTH_SHORT).show()
            // Thêm code để mở màn hình Quản lý dịch vụ tại đây
        }
        btnManageServices.setOnClickListener {
            val intent = Intent(this, Dichvu::class.java)
            startActivity(intent)
        }
        btnRevenue.setOnClickListener {
            Toast.makeText(this, "Bạn đã nhấn Doanh thu", Toast.LENGTH_SHORT).show()
            // Thêm code để mở màn hình Doanh thu tại đây
        }
        btnRevenue.setOnClickListener {
            val intent = Intent(this, DoanhThuActivity::class.java)
            startActivity(intent)
        }
        // Thiết lập OnClickListener cho nút Logout mới
        btnLogout.setOnClickListener {
            Toast.makeText(this, "Đang thực hiện Logout...", Toast.LENGTH_SHORT).show()
            // Thêm code xử lý đăng xuất tại đây (ví dụ: chuyển về màn hình Login)
        }
        btnLogout.setOnClickListener {
            val intent = Intent(this, logout::class.java)
            startActivity(intent)
        }
        // Thiết lập OnClickListener cho mục Trang chủ (Bottom Navigation Bar)
        navHome.setOnClickListener {
            Toast.makeText(this, "Bạn đã nhấn Trang chủ", Toast.LENGTH_SHORT).show()
            // Cập nhật giao diện nếu mục này được chọn
        }

        // Đã xóa phần xử lý navAccount và WindowInsets không cần thiết
        val logoutTextView = findViewById<TextView>(R.id.btnLogout)
        logoutTextView.setOnClickListener {

            val intent = Intent(this, logout::class.java)
            startActivity(intent)
        }
    }
}