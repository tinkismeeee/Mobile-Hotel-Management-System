package com.example.androidproject

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.models.BookingHistoryResponse

class MyBookingAdapter(private val bookings: List<BookingHistoryResponse>) :
    RecyclerView.Adapter<MyBookingAdapter.ViewHolder>() {

    // Ánh xạ đúng ID từ file my_booking_item.xml
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_booking_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bookings[position]

        // Xử lý hiển thị ngày
        val dateStr = try {
            "${formatDate(item.check_in)} - ${formatDate(item.check_out)}"
        } catch (e: Exception) {
            "${item.check_in} - ${item.check_out}"
        }

        holder.tvDate.text = dateStr
        holder.tvInfo.text = "${item.total_guests} Khách • ${item.hotel_name ?: "Khách sạn"}"

        // Xử lý màu sắc trạng thái
        val status = item.status ?: "Unknown"
        holder.tvStatus.text = status

        when (status.lowercase()) {
            "confirmed" -> holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Xanh lá
            "pending" -> holder.tvStatus.setTextColor(Color.parseColor("#FF9800"))   // Cam
            "completed" -> holder.tvStatus.setTextColor(Color.GRAY)                  // Xám
            "cancelled" -> holder.tvStatus.setTextColor(Color.RED)                   // Đỏ
            else -> holder.tvStatus.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount() = bookings.size

    // Hàm phụ trợ format ngày (yyyy-MM-dd -> dd/MM/yyyy)
    private fun formatDate(isoString: String?): String {
        if (isoString == null) return ""
        return try {
            val ymd = isoString.split("T")[0]
            val parts = ymd.split("-")
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } catch (e: Exception) {
            isoString
        }
    }
}