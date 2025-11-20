package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.ChipGroup

class MyFavoriteActivity : AppCompatActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var favoriteAdapter: FavoriteHotelAdapter
    private lateinit var hotelList: MutableList<FavoriteHotel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite)

        // 1. Tìm Views
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        val chipGroup: ChipGroup = findViewById(R.id.chip_group_filter)
        rvFavorites = findViewById(R.id.rv_favorites)

        // 2. Thiết lập Toolbar
        toolbar.setNavigationOnClickListener {
            finish() // Quay lại
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    Toast.makeText(this, "Tìm kiếm...", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // 3. Thiết lập RecyclerView
        setupRecyclerView()

        // 4. Xử lý Chip clicks
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_all -> filterList("All")
                R.id.chip_popular -> filterList("Popular")
                R.id.chip_recommended -> filterList("Recommended")
                R.id.chip_nearest -> filterList("Nearest")
            }
        }
    }

    private fun setupRecyclerView() {
        // Tạm thời dùng ảnh placeholder
        // Bạn cần thêm các ảnh này vào drawable
        hotelList = mutableListOf(
            FavoriteHotel("Citadines Sukhumvit", 4.7, "Bangkok", 66, R.drawable.hotel_placeholder),
            FavoriteHotel("Caravelle Saigon", 4.9, "Ho Chi Minh", 120, R.drawable.map_placeholder),
            FavoriteHotel("Double Tree Hotel", 4.5, "Da Nang", 88, R.drawable.hotel_placeholder)
        )

        favoriteAdapter = FavoriteHotelAdapter(hotelList)
        rvFavorites.apply {
            layoutManager = LinearLayoutManager(this@MyFavoriteActivity)
            adapter = favoriteAdapter
        }
    }

    private fun filterList(criteria: String) {
        // TODO: Thêm logic lọc danh sách `hotelList` dựa trên `criteria`
        // Sau khi lọc, gọi `favoriteAdapter.notifyDataSetChanged()`
        Toast.makeText(this, "Đang lọc theo: $criteria", Toast.LENGTH_SHORT).show()
    }
}