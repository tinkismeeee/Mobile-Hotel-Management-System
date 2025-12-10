package com.example.androidproject

import BookingRealm
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.query.RealmResults
import java.text.SimpleDateFormat
import java.util.Locale

class BookedAdapter(private var bookings: List<BookingRealm>) :
    RecyclerView.Adapter<BookedAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomImage: ImageView = view.findViewById(R.id.roomImg)
        val roomNumber: TextView = view.findViewById(R.id.textviewRoomNumber)
        val floor: TextView = view.findViewById(R.id.textviewFloor)
        val roomType: TextView = view.findViewById(R.id.textviewRoomType)
        val services: TextView = view.findViewById(R.id.tvServices)
        val maxGuests: TextView = view.findViewById(R.id.textviewMaxGuests)
        val bedCount: TextView = view.findViewById(R.id.textviewBedCount)
        val dateRange: TextView = view.findViewById(R.id.tvDaterange)
        val price: TextView = view.findViewById(R.id.textviewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.booked_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]
        holder.roomNumber.text = "Room: ${booking.roomNumber}"
        holder.floor.text = "Floor ${booking.floor}"
        holder.roomType.text = booking.roomType
        holder.maxGuests.text = "Max guests: ${booking.maxGuests}"
        holder.bedCount.text = "Bed count: ${booking.bedCount}"
        val formattedCheckIn = formatDate(booking.checkIn)
        val formattedCheckOut = formatDate(booking.checkOut)
        holder.dateRange.text = "Date: $formattedCheckIn to $formattedCheckOut"
        val context = holder.itemView.context
        val randomIndex = (1..35).random()
        val imageName = "room_image_$randomIndex"
        val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        holder.roomImage.setImageResource(
            if (imageResId != 0) imageResId else R.drawable.room_image_1
        )
        if (booking.services.isNotEmpty()) {
            val serviceNames = mapServiceCodesToNames(booking.services)
            holder.services.text = "Services:\n- ${serviceNames.joinToString("\n- ")}"
            holder.services.visibility = View.VISIBLE
        } else {
            holder.services.visibility = View.GONE
        }
        val nights = calculateNights(booking.checkIn, booking.checkOut)
        val serviceTotal = calculateServiceTotal(booking.services)
        val roomTotal = booking.price * nights
        val finalTotal = roomTotal + serviceTotal
        holder.price.text ="Total: ${formatMoney(finalTotal)} VNĐ"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, checkout::class.java)
            intent.putExtra("roomNumber", booking.roomNumber)
            intent.putExtra("floor", booking.floor)
            intent.putExtra("roomType", booking.roomType)
            intent.putExtra("maxGuests", booking.maxGuests)
            intent.putExtra("bedCount", booking.bedCount)
            intent.putExtra("totalPrice", formatMoney(finalTotal))
            intent.putStringArrayListExtra("listOfService", ArrayList(booking.services))
            intent.putExtra("roomImageId", imageResId)
            intent.putExtra("checkIn", booking.checkIn)
            intent.putExtra("checkOut", booking.checkOut)
            Log.i("DEBUG", "${booking.roomNumber} | ${booking.floor} | ${booking.roomType} | ${booking.maxGuests} | ${booking.bedCount} | ${booking.services} | ${formatMoney(finalTotal)} | ${booking.checkIn} | ${booking.checkOut} | ${imageResId}" )
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = bookings.size

    fun updateData(newBookings: RealmResults<BookingRealm>) {
        this.bookings = newBookings
        notifyDataSetChanged()
    }
    private fun mapServiceCodesToNames(serviceCodes: List<String>): List<String> {
        val serviceMap = mapOf(
            "SV001" to "Laundry and ironing service",
            "SV002" to "Buffet breakfast",
            "SV003" to "Pickup from airport",
            "SV004" to "Relaxing massage and spa",
            "SV005" to "Dinner buffet at restaurant",
            "SV006" to "In-room mini bar",
            "SV007" to "Additional bed for extra guest",
            "SV008" to "Daily city tour guide"
        )
        return serviceCodes.map { code -> serviceMap[code] ?: "Unknown service" }
    }
    private val servicePriceMap = mapOf(
        "SV001" to 50000,
        "SV002" to 100000,
        "SV003" to 200000,
        "SV004" to 300000,
        "SV005" to 250000,
        "SV006" to 150000,
        "SV007" to 200000,
        "SV008" to 500000
    )

    private fun calculateServiceTotal(services: List<String>): Int {
        return services.sumOf { servicePriceMap[it] ?: 0 }
    }
    private fun calculateNights(checkIn: String, checkOut: String): Int {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start = format.parse(checkIn)
        val end = format.parse(checkOut)

        val diff = end.time - start.time
        return (diff / (1000L * 60 * 60 * 24)).toInt()
    }
    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            if (date != null) outputFormat.format(date) else dateString
        } catch (e: Exception) {
            dateString
        }
    }
    private fun formatMoney(amount: Int): String {
        return "%,d".format(amount).replace(",", ".")
    }
}
