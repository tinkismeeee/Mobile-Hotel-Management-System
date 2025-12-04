package com.example.androidproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.api.RetrofitClient
import com.example.androidproject.models.BookingHistoryResponse
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBookingFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private var fullList: List<BookingHistoryResponse> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        recyclerView = view.findViewById(R.id.rvMyBookings)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 1. Tải dữ liệu khi mở màn hình
        loadBookings()

        // 2. Xử lý khi bấm chuyển Tab
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filterList(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadBookings() {
        RetrofitClient.instance.getBookingHistory().enqueue(object : Callback<List<BookingHistoryResponse>> {
            override fun onResponse(call: Call<List<BookingHistoryResponse>>, response: Response<List<BookingHistoryResponse>>) {
                if (response.isSuccessful) {
                    fullList = response.body() ?: emptyList()
                    // Mặc định hiển thị tab đầu tiên (Sắp tới)
                    filterList(0)
                } else {
                    Toast.makeText(context, "Lỗi tải lịch sử", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BookingHistoryResponse>>, t: Throwable) {
                // Demo dữ liệu giả nếu lỗi mạng
                fullList = getFakeBookings()
                filterList(0)
                Toast.makeText(context, "Đang hiển thị dữ liệu mẫu", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterList(tabIndex: Int) {
        val filteredList = if (tabIndex == 0) {
            // Tab 0: Sắp tới (Confirmed, Pending)
            fullList.filter { it.status == "confirmed" || it.status == "pending" }
        } else {
            // Tab 1: Lịch sử (Completed, Cancelled)
            fullList.filter { it.status == "completed" || it.status == "cancelled" }
        }

        recyclerView.adapter = MyBookingAdapter(filteredList)
    }

    // Hàm tạo dữ liệu giả (Để demo không bị trắng màn hình)
    private fun getFakeBookings(): List<BookingHistoryResponse> {
        return listOf(
            BookingHistoryResponse(1, 11, "2025-11-24", "2025-12-24", "2025-12-26", "confirmed", 2, "user11"),
            BookingHistoryResponse(2, 11, "2025-10-10", "2025-10-15", "2025-10-18", "completed", 1, "user11"),
            BookingHistoryResponse(3, 11, "2025-11-30", "2025-12-30", "2025-12-31", "pending", 4, "user11")
        )
    }
}