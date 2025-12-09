package com.example.androidproject

import BookingRealm
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.ext.query

class my_booking_fragment : Fragment() {
    private val realm by lazy { App.realm }
    private lateinit var bookedAdapter: BookedAdapter
    private lateinit var bookedListRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_booking_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        loadBookedRooms()
    }

    private fun setupRecyclerView(view: View) {
        bookedListRecyclerView = view.findViewById(R.id.booked_list)
        bookedAdapter = BookedAdapter(emptyList())
        bookedListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookedListRecyclerView.adapter = bookedAdapter
    }
    private fun loadBookedRooms() {
        val bookedList = realm.query<BookingRealm>().find()
        Log.d("MyBookingFragment", "Found ${bookedList.size} total bookings in Realm.")
        bookedAdapter.updateData(bookedList)
    }
}
