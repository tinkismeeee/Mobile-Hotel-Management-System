package com.example.androidproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class home_list_all_recycleview_item_adapter(private val list_all_villas_and_hotels: List<villas_and_hotels_list_item>) :
    RecyclerView.Adapter<home_list_all_recycleview_item_adapter.list_all_villas_and_hotels_ViewHolder>() {

    class list_all_villas_and_hotels_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelImage: ImageView = itemView.findViewById(R.id.ivHotelImage)
        val hotelName: TextView = itemView.findViewById(R.id.tvHotelName)
        val rating: TextView = itemView.findViewById(R.id.tvRating)
        val location: TextView = itemView.findViewById(R.id.tvLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): list_all_villas_and_hotels_ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_list_all_recycleview_item, parent, false)
        return list_all_villas_and_hotels_ViewHolder(view)
    }

    override fun getItemCount() = list_all_villas_and_hotels.size

    override fun onBindViewHolder(holder: list_all_villas_and_hotels_ViewHolder, position: Int) {
        val currentItem = list_all_villas_and_hotels[position]

        holder.hotelName.text = currentItem.hotelName
        holder.rating.text = currentItem.rating.toString()
        holder.location.text = currentItem.location

        if (currentItem.photoReference != null) {
            val apiKey = "AIzaSyBBa7totCeTLg3APty-NckqqQK8nnRhrJc"
            val photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${currentItem.photoReference}&key=$apiKey"
            Glide.with(holder.itemView.context).load(photoUrl).centerCrop().placeholder(R.drawable.hotel_img).into(holder.hotelImage)
        } else {
            holder.hotelImage.setImageResource(R.drawable.hotel_img)
        }

        // Sự kiện click vào khách sạn
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            // [QUAN TRỌNG] Chuyển sang RoomListActivity
            val intent = Intent(context, RoomListActivity::class.java)

            // Truyền tên khách sạn
            intent.putExtra("HOTEL_NAME", currentItem.hotelName)

            context.startActivity(intent)
        }
    }
}