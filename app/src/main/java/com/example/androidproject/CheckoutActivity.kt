package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.api.RetrofitClient
import com.example.androidproject.models.*
import com.example.androidproject.utils.CurrentUser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvHotelName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvDates: TextView
    private lateinit var tvGuests: TextView
    private lateinit var btnConfirmPayment: Button
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvTotal: TextView
    private lateinit var etPromoCode: EditText
    private lateinit var btnApplyPromo: Button
    private lateinit var tvDiscount: TextView
    private lateinit var rowDiscount: LinearLayout
    private lateinit var cbAgreement: CheckBox
    private lateinit var rvServices: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter

    private var listServicesAPI: List<ServiceResponse> = ArrayList()
    private lateinit var allowedServiceCodes: ArrayList<String>
    private val currencyFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initViews()

        val hotelName = intent.getStringExtra("HOTEL_NAME")
        val location = intent.getStringExtra("HOTEL_LOCATION")
        val checkInDate = intent.getStringExtra("CHECK_IN_DATE") ?: "24-12-2025"
        val checkOutDate = intent.getStringExtra("CHECK_OUT_DATE") ?: "26-12-2025"
        val roomId = intent.getIntExtra("ROOM_ID", 1)

        allowedServiceCodes = intent.getStringArrayListExtra("ALLOWED_SERVICES") ?: arrayListOf()

        tvHotelName.text = hotelName
        tvLocation.text = location
        tvDates.text = "$checkInDate - $checkOutDate"
        tvGuests.text = "2 Khách"

        rvServices.layoutManager = LinearLayoutManager(this)
        loadServicesAndFilter()

        // Tạm thời comment dòng này nếu Backend chưa có API preview
        // loadRealInvoiceData(roomId, checkInDate, checkOutDate, null)

        btnBack.setOnClickListener { finish() }

        btnApplyPromo.setOnClickListener {
            Toast.makeText(this, "Chức năng đang bảo trì", Toast.LENGTH_SHORT).show()
        }

        cbAgreement.setOnCheckedChangeListener { _, isChecked ->
            btnConfirmPayment.isEnabled = isChecked
            btnConfirmPayment.alpha = if (isChecked) 1.0f else 0.5f
        }

        btnConfirmPayment.setOnClickListener {
            processBooking(roomId, checkInDate, checkOutDate)
        }
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tvHotelName = findViewById(R.id.tvHotelName)
        tvLocation = findViewById(R.id.tvLocation)
        tvDates = findViewById(R.id.tvDates)
        tvGuests = findViewById(R.id.tvGuests)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)
        tvRoomPrice = findViewById(R.id.tvRoomPrice)
        tvTaxes = findViewById(R.id.tvTaxes)
        tvTotal = findViewById(R.id.tvTotal)
        etPromoCode = findViewById(R.id.etPromoCode)
        btnApplyPromo = findViewById(R.id.btnApplyPromo)
        tvDiscount = findViewById(R.id.tvDiscount)
        rowDiscount = findViewById(R.id.rowDiscount)
        cbAgreement = findViewById(R.id.cbAgreement)
        rvServices = findViewById(R.id.rvServices)
        btnConfirmPayment.isEnabled = false
        btnConfirmPayment.alpha = 0.5f
    }

    private fun loadServicesAndFilter() {
        RetrofitClient.instance.getServices().enqueue(object : Callback<List<ServiceResponse>> {
            override fun onResponse(call: Call<List<ServiceResponse>>, response: Response<List<ServiceResponse>>) {
                if (response.isSuccessful) {
                    val allServices = response.body() ?: emptyList()
                    val filteredServices = allServices.filter { service ->
                        allowedServiceCodes.contains(service.serviceCode)
                    }
                    if (filteredServices.isNotEmpty()) {
                        listServicesAPI = filteredServices
                        serviceAdapter = ServiceAdapter(listServicesAPI)
                        rvServices.adapter = serviceAdapter
                    }
                }
            }
            override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {}
        })
    }

    private fun processBooking(roomId: Int, checkInStr: String, checkOutStr: String) {
        // 1. Lấy danh sách dịch vụ
        val selectedServices = serviceAdapter.getSelectedServices().map {
            BookingServiceItem(serviceCode = it.serviceCode, quantity = it.quantity)
        }

        val checkIn = convertDate(checkInStr)
        val checkOut = convertDate(checkOutStr)
        val promoCode = etPromoCode.text.toString().trim().ifEmpty { null }

        // 2. Tạo Request
        val request = CreateBookingRequest(
            user_id = CurrentUser.id,
            room_ids = listOf(roomId),
            check_in = checkIn,
            check_out = checkOut,
            total_guests = 2,
            services = selectedServices,
            promotionCode = promoCode
        )

        // 3. Gửi lên Server
        RetrofitClient.instance.createBooking(request = request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@CheckoutActivity, BookingSuccessfulActivity::class.java)
                    intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
                    intent.putExtra("TOTAL", "Đặt thành công")
                    startActivity(intent)
                    finish()
                } else {
                    // Hiện thông báo lỗi chi tiết từ Server
                    val errorMsg = try { response.errorBody()?.string() } catch (e: Exception) { "Lỗi không xác định" }
                    Toast.makeText(this@CheckoutActivity, "Lỗi (${response.code()}): $errorMsg", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun convertDate(dateStr: String): String {
        return try {
            val parts = dateStr.split("-")
            if (parts.size == 3) "${parts[2]}-${parts[1]}-${parts[0]}" else dateStr
        } catch (e: Exception) { dateStr }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}