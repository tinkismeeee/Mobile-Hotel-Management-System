package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.models.ServiceResponse
import java.text.DecimalFormat

class ServiceAdapter(private val services: List<ServiceResponse>) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvServiceName)
        val price: TextView = itemView.findViewById(R.id.tvServicePrice)
        val quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = services[position]
        val format = DecimalFormat("#,###")

        holder.name.text = item.name
        holder.price.text = "${format.format(item.price)} đ"
        holder.quantity.text = item.quantity.toString()

        holder.btnPlus.setOnClickListener {
            item.quantity++
            holder.quantity.text = item.quantity.toString()
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                holder.quantity.text = item.quantity.toString()
            }
        }
    }

    override fun getItemCount() = services.size

    fun getSelectedServices(): List<ServiceResponse> {
        return services.filter { it.quantity > 0 }
    }
}