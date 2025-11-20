package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R

class FavoriteHotelAdapter(private val hotelList: List<FavoriteHotel>) :
    RecyclerView.Adapter<FavoriteHotelAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelImage: ImageView = itemView.findViewById(R.id.iv_hotel_image)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.iv_favorite_icon)
        val hotelName: TextView = itemView.findViewById(R.id.tv_hotel_name)
        val rating: TextView = itemView.findViewById(R.id.tv_rating)
        val location: TextView = itemView.findViewById(R.id.tv_location)
        val price: TextView = itemView.findViewById(R.id.tv_price)

        fun bind(hotel: FavoriteHotel) {
            hotelImage.setImageResource(hotel.imageResId)
            hotelName.text = hotel.name
            rating.text = hotel.rating.toString()
            location.text = hotel.location
            price.text = "$${hotel.pricePerNight} /night"

            // Xử lý icon yêu thích (nếu cần)
            if (hotel.isFavorite) {
                favoriteIcon.visibility = View.VISIBLE
            } else {
                favoriteIcon.visibility = View.GONE
            }

            // Xử lý click (nếu cần)
            itemView.setOnClickListener {
                // TODO: Mở chi tiết khách sạn
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_hotel, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(hotelList[position])
    }

    override fun getItemCount() = hotelList.size
}