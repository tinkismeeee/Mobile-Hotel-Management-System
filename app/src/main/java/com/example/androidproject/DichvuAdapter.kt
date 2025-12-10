package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiceAdapter(
    private val serviceList: MutableList<Service>,
    private val onItemClick: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.tvServiceName)
        val tvCode: TextView = itemView.findViewById(R.id.tvServiceCode)
        val tvId: TextView = itemView.findViewById(R.id.tvServiceId)
        val tvPrice: TextView = itemView.findViewById(R.id.tvServicePrice)
        val tvAvailability: TextView = itemView.findViewById(R.id.tvServiceAvailability)
        val tvDescription: TextView = itemView.findViewById(R.id.tvServiceDescription)

        init {
            itemView.setOnClickListener {
                onItemClick(serviceList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_dichvu_adapter, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = serviceList[position]

        holder.tvName.text = item.name
        holder.tvCode.text = "Code: ${item.service_code}"
        holder.tvId.text = "ID: ${item.service_id}"
        holder.tvPrice.text = "Price: ${item.price} đ"
        holder.tvAvailability.text = "Available: ${if (item.availability) "Yes" else "No"}"
        holder.tvDescription.text = "Description: ${item.description}"
    }

    override fun getItemCount(): Int = serviceList.size

    fun updateData(newList: List<Service>) {
        serviceList.clear()
        serviceList.addAll(newList)
        notifyDataSetChanged()
    }
}
