package com.example.androidproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyBookingAdapter(private val bookingList: List<Booking>) :
    RecyclerView.Adapter<MyBookingAdapter.BookingViewHolder>() {
    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelImage: ImageView = itemView.findViewById(R.id.ivHotelImage)
        val hotelName: TextView = itemView.findViewById(R.id.tvHotelName)
        val rating: TextView = itemView.findViewById(R.id.tvRating)
        val location: TextView = itemView.findViewById(R.id.tvLocation)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val datesValue: TextView = itemView.findViewById(R.id.tvDatesValue)
        val guestValue: TextView = itemView.findViewById(R.id.tvGuestValue)
    }

    /**
     * Tạo ViewHolder mới
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_booking_item, parent, false)
        return BookingViewHolder(view)
    }

    /**
     * Lấy tổng số mục
     */
    override fun getItemCount(): Int {
        return bookingList.size
    }

    /**
     * Gán dữ liệu vào View và XỬ LÝ CLICK
     */
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]

        // 1. Gán dữ liệu (chữ + ảnh)
        holder.hotelName.text = booking.hotelName
        holder.rating.text = booking.rating.toString()
        holder.location.text = booking.location
        holder.price.text = "$${booking.pricePerNight} /night"
        holder.datesValue.text = "${booking.startDate} - ${booking.endDate}"
        holder.guestValue.text = "${booking.guests} Guests (${booking.rooms} Room)"
        holder.hotelImage.setImageResource(R.drawable.avt)

        // 2. Xử lý sự kiện CLICK
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, RoomDetailActivity::class.java)
            intent.putExtra("HOTEL_NAME", booking.hotelName)
            intent.putExtra("HOTEL_LOCATION", booking.location)
            intent.putExtra("HOTEL_RATING", booking.rating.toString())
            intent.putExtra("CHECK_IN_DATE", booking.startDate)
            intent.putExtra("CHECK_OUT_DATE", booking.endDate)

            context.startActivity(intent)
        }
    }
}