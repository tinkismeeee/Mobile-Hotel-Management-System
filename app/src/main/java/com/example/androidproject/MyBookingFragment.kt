package com.example.androidproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText

class MyBookingFragment : Fragment() {

    // === KHAI BÁO BIẾN ===
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyBookingAdapter
    private lateinit var tabLayout: TabLayout

    // Danh sách "gốc" chứa tất cả dữ liệu
    private var allBookingsList = mutableListOf<Booking>()
    // Danh sách "hiển thị" (đã lọc)
    private var displayedBookingList = mutableListOf<Booking>()
    // ====================

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Tải layout cho "My Booking"
        return inflater.inflate(R.layout.fragment_my_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // === CODE LOGIC KẾT NỐI GIAO DIỆN ===

        // 1. Tạo dữ liệu giả và lưu vào danh sách "gốc"
        createMockData()

        // 2. Tìm các View từ layout (dựa trên ID trong file .xml)
        recyclerView = view.findViewById(R.id.recyclerViewBookings)
        tabLayout = view.findViewById(R.id.tabLayout)

        // 3. Thiết lập RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Khởi tạo Adapter với danh sách "hiển thị"
        displayedBookingList.clear()
        displayedBookingList.addAll(allBookingsList)

        adapter = MyBookingAdapter(displayedBookingList)
        recyclerView.adapter = adapter


    }

    private fun createMockData() {
        allBookingsList.clear() // Xóa dữ liệu cũ (nếu có)
        allBookingsList.add(
            Booking(
                hotelName = "The Aston Vill Hotel",
                rating = 4.7,
                location = "Veum Point, Michikoton",
                pricePerNight = 120,
                startDate = "12",
                endDate = "14 Nov 2024",
                guests = 2,
                rooms = 1,
                status = "booked" // <-- Status cho Tab "Booked"
            )
        )
        allBookingsList.add(
            Booking(
                hotelName = "Mystic Palms",
                rating = 4.0,
                location = "Palm Springs, CA",
                pricePerNight = 230,
                startDate = "20",
                endDate = "25 Nov 2024",
                guests = 1,
                rooms = 1,
                status = "history" // <-- Status cho Tab "History"
            )
        )
        allBookingsList.add(
            Booking(
                hotelName = "Elysian Suites",
                rating = 3.8,
                location = "San Diego, CA",
                pricePerNight = 320,
                startDate = "27",
                endDate = "28 Nov 2024",
                guests = 1,
                rooms = 1,
                status = "booked" // <-- Status cho Tab "Booked"
            )
        )
    }

    /**
     * Gắn bộ lắng nghe cho TabLayout
     */


    /**
     * Gắn bộ lắng nghe cho Thanh tìm kiếm
     */

    /**
     * HÀM QUAN TRỌNG: "BỘ NÃO" CỦA VIỆC LỌC
     * Lọc danh sách `allBookingsList` dựa trên tab và tìm kiếm,
     * sau đó cập nhật `displayedBookingList`
     */
}