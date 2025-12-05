package com.example.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoanhthuAdapter(
    private val invoiceList: MutableList<Invoice>,
    private val onItemClick: (Invoice) -> Unit
) : RecyclerView.Adapter<DoanhthuAdapter.InvoiceViewHolder>() {

    inner class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivInvoiceIcon: ImageView = itemView.findViewById(R.id.ivInvoiceIcon)
        val tvRoomNumber: TextView = itemView.findViewById(R.id.tvRoomNumber)
        val tvInvoiceId: TextView = itemView.findViewById(R.id.tvInvoiceId)

        init {
            itemView.setOnClickListener {
                onItemClick(invoiceList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_doanhthu_adapter, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = invoiceList[position]
        holder.tvRoomNumber.text = "Room Number: ${invoice.booking_id}"
        holder.tvInvoiceId.text = "Invoice ID: ${invoice.invoice_id}"
    }

    override fun getItemCount(): Int = invoiceList.size

    fun updateData(newList: List<Invoice>) {
        invoiceList.clear()
        invoiceList.addAll(newList)
        notifyDataSetChanged()
    }
}
