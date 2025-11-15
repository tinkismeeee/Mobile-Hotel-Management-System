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
// === CÁC IMPORT CẦN THÊM ===
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
// ==========================

class MyBookingFragment : Fragment() {

    // === KHAI BÁO BIẾN ===
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyBookingAdapter
    private lateinit var searchEditText: TextInputEditText
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
        searchEditText = view.findViewById(R.id.etSearch)
        tabLayout = view.findViewById(R.id.tabLayout)

        // 3. Thiết lập RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Khởi tạo Adapter với danh sách "hiển thị"
        adapter = MyBookingAdapter(displayedBookingList)
        recyclerView.adapter = adapter

        // 4. Thêm Listeners (Bộ lắng nghe) cho Tabs và Search
        setupTabListener()
        setupSearchListener()

        // 5. Lọc và hiển thị danh sách lần đầu (mặc định là tab "Booked")
        filterAndDisplayList()
    }

    /**
     * Hàm này tạo 3 mục đặt phòng giả VÀ THÊM TRẠNG THÁI (STATUS)
     */
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
    private fun setupTabListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Khi người dùng chọn tab, lọc và hiển thị lại danh sách
                filterAndDisplayList()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * Gắn bộ lắng nghe cho Thanh tìm kiếm
     */
    private fun setupSearchListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Khi người dùng gõ tìm kiếm, lọc và hiển thị lại danh sách
                filterAndDisplayList()
            }
        })
    }

    /**
     * HÀM QUAN TRỌNG: "BỘ NÃO" CỦA VIỆC LỌC
     * Lọc danh sách `allBookingsList` dựa trên tab và tìm kiếm,
     * sau đó cập nhật `displayedBookingList`
     */
    private fun filterAndDisplayList() {
        // Lấy tab đang được chọn (0 = Booked, 1 = History)
        val selectedTabPosition = tabLayout.selectedTabPosition
        val currentStatusFilter = if (selectedTabPosition == 0) "booked" else "history"

        // Lấy nội dung tìm kiếm
        val searchQuery = searchEditText.text.toString().lowercase().trim()

        // 1. Lọc theo Tab
        val tabFilteredList = allBookingsList.filter { booking ->
            booking.status == currentStatusFilter
        }

        // 2. Lọc theo Tìm kiếm (từ kết quả đã lọc ở trên)
        val searchFilteredList = if (searchQuery.isEmpty()) {
            tabFilteredList // Nếu không tìm gì, lấy luôn kết quả lọc tab
        } else {
            tabFilteredList.filter { booking ->
                // Tìm kiếm theo tên khách sạn
                booking.hotelName?.lowercase()?.contains(searchQuery) == true
            }
        }

        // 3. Cập nhật RecyclerView
        displayedBookingList.clear()
        displayedBookingList.addAll(searchFilteredList)
        adapter.notifyDataSetChanged() // Báo cho adapter "vẽ" lại danh sách
    }
}