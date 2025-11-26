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
import com.example.androidproject.utils.MockData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class CheckoutActivity : AppCompatActivity() {

    // 1. Khai báo các thành phần giao diện
    private lateinit var btnBack: ImageButton
    private lateinit var tvHotelName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvDates: TextView
    private lateinit var tvGuests: TextView
    private lateinit var btnConfirmPayment: Button

    // Các trường hiển thị giá tiền
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvTotal: TextView

    // Các trường nhập liệu mới (Mã giảm giá, Checkbox)
    private lateinit var etPromoCode: EditText
    private lateinit var btnApplyPromo: Button
    private lateinit var tvDiscount: TextView
    private lateinit var rowDiscount: LinearLayout
    private lateinit var cbAgreement: CheckBox

    // [QUAN TRỌNG] Danh sách dịch vụ
    private lateinit var rvServices: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private var listServicesAPI: List<ServiceResponse> = ArrayList()

    // Biến lưu dữ liệu được truyền từ màn hình trước
    private lateinit var allowedServiceCodes: ArrayList<String> // Danh sách dịch vụ được phép dùng
    private val currencyFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Khởi tạo các View
        initViews()

        // 2. Nhận dữ liệu từ Intent (Được truyền từ RoomDetailActivity)
        val hotelName = intent.getStringExtra("HOTEL_NAME")
        val location = intent.getStringExtra("HOTEL_LOCATION")
        val checkInDate = intent.getStringExtra("CHECK_IN_DATE") ?: "24-12-2025"
        val checkOutDate = intent.getStringExtra("CHECK_OUT_DATE") ?: "26-12-2025"
        val roomId = intent.getIntExtra("ROOM_ID", 1)

        // [MỚI] Nhận danh sách mã dịch vụ mà phòng này hỗ trợ (VD: SV001, SV002)
        // Nếu không có (null), mặc định lấy danh sách rỗng
        allowedServiceCodes = intent.getStringArrayListExtra("ALLOWED_SERVICES") ?: arrayListOf()

        // 3. Hiển thị thông tin cơ bản lên màn hình
        tvHotelName.text = hotelName
        tvLocation.text = location
        tvDates.text = "$checkInDate - $checkOutDate"
        tvGuests.text = "2 Khách"

        // 4. Cấu hình danh sách dịch vụ (RecyclerView)
        rvServices.layoutManager = LinearLayoutManager(this)

        // Gọi hàm tải dịch vụ (có lọc theo phòng và fallback dữ liệu giả)
        loadServicesAndFilter()

        // 5. Gọi API tính tiền phòng (Lần đầu chưa có mã giảm giá)
        loadRealInvoiceData(roomId, checkInDate, checkOutDate, null)

        // 6. Xử lý các sự kiện Click
        btnBack.setOnClickListener { finish() }

        // Xử lý nút áp dụng Mã giảm giá
        btnApplyPromo.setOnClickListener {
            val code = etPromoCode.text.toString().trim()
            if (code.isNotEmpty()) {
                // Gọi lại API tính tiền với mã giảm giá
                loadRealInvoiceData(roomId, checkInDate, checkOutDate, code)
                hideKeyboard()
            } else {
                Toast.makeText(this, "Vui lòng nhập mã", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý Checkbox điều khoản (Bắt buộc chọn mới sáng nút thanh toán)
        cbAgreement.setOnCheckedChangeListener { _, isChecked ->
            btnConfirmPayment.isEnabled = isChecked
            btnConfirmPayment.alpha = if (isChecked) 1.0f else 0.5f
        }

        // Xử lý nút Xác nhận thanh toán
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

        // Mặc định nút thanh toán bị mờ và không bấm được
        btnConfirmPayment.isEnabled = false
        btnConfirmPayment.alpha = 0.5f
    }

    // === 1. TẢI DỊCH VỤ & LỌC THEO PHÒNG ===
    private fun loadServicesAndFilter() {
        // Gọi API lấy tất cả dịch vụ hiện có
        RetrofitClient.instance.getServices().enqueue(object : Callback<List<ServiceResponse>> {
            override fun onResponse(call: Call<List<ServiceResponse>>, response: Response<List<ServiceResponse>>) {
                if (response.isSuccessful) {
                    val allServices = response.body() ?: emptyList()

                    // [LOGIC QUAN TRỌNG] Lọc: Chỉ lấy những dịch vụ có trong allowedServiceCodes
                    val filteredServices = allServices.filter { service ->
                        allowedServiceCodes.contains(service.serviceCode)
                    }

                    if (filteredServices.isNotEmpty()) {
                        setupServiceList(filteredServices)
                    } else {
                        // API rỗng hoặc lọc xong không còn gì -> Dùng Mock Data
                        loadMockServicesFiltered()
                    }
                } else {
                    // API lỗi -> Dùng Mock Data
                    loadMockServicesFiltered()
                }
            }
            override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {
                // Mất mạng -> Dùng Mock Data
                loadMockServicesFiltered()
            }
        })
    }

    // Hàm dự phòng: Load dữ liệu giả và cũng lọc theo phòng
    private fun loadMockServicesFiltered() {
        Toast.makeText(this, "Đang hiển thị dịch vụ mẫu (Offline Mode)", Toast.LENGTH_SHORT).show()
        val mockServices = MockData.getMockServices().filter {
            allowedServiceCodes.contains(it.serviceCode)
        }
        setupServiceList(mockServices)
    }

    // Hàm hiển thị danh sách lên giao diện
    private fun setupServiceList(list: List<ServiceResponse>) {
        listServicesAPI = list
        serviceAdapter = ServiceAdapter(listServicesAPI)
        rvServices.adapter = serviceAdapter
    }

    // === 2. TÍNH TIỀN (GỌI API INVOICE PREVIEW) ===
    private fun loadRealInvoiceData(roomId: Int, checkIn: String, checkOut: String, promoCode: String?) {
        val safeCheckIn = convertDate(checkIn)
        val safeCheckOut = convertDate(checkOut)

        val request = InvoicePreviewRequest(
            bookingId = null,
            roomIds = listOf(roomId),
            checkIn = safeCheckIn,
            checkOut = safeCheckOut,
            promoCode = promoCode
        )

        RetrofitClient.instance.previewInvoice(request).enqueue(object : Callback<InvoiceResponse> {
            override fun onResponse(call: Call<InvoiceResponse>, response: Response<InvoiceResponse>) {
                if (response.isSuccessful) {
                    val invoice = response.body()
                    invoice?.let {
                        // Cập nhật UI với giá tiền từ Server
                        tvRoomPrice.text = "${currencyFormat.format(it.totalRoomCost)} VND"
                        tvTaxes.text = "${currencyFormat.format(it.vatAmount)} VND"
                        tvTotal.text = "${currencyFormat.format(it.finalTotal)} VND"

                        // Hiển thị dòng giảm giá nếu có
                        if (it.discountAmount > 0) {
                            rowDiscount.visibility = View.VISIBLE
                            tvDiscount.text = "- ${currencyFormat.format(it.discountAmount)} VND"
                            Toast.makeText(this@CheckoutActivity, "Áp dụng mã thành công!", Toast.LENGTH_SHORT).show()
                        } else {
                            rowDiscount.visibility = View.GONE
                            if (promoCode != null) {
                                Toast.makeText(this@CheckoutActivity, "Mã không hợp lệ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<InvoiceResponse>, t: Throwable) {
                // Nếu lỗi tính tiền, có thể giữ nguyên giá trị mặc định hoặc hiện thông báo
            }
        })
    }

    // === 3. XỬ LÝ ĐẶT PHÒNG (GỌI API CREATE BOOKING) ===
    private fun processBooking(roomId: Int, checkInStr: String, checkOutStr: String) {
        // A. Lấy danh sách dịch vụ người dùng đã chọn (số lượng > 0)
        val selectedServices = serviceAdapter.getSelectedServices().map {
            BookingServiceItem(serviceCode = it.serviceCode, quantity = it.quantity)
        }

        // B. Chuẩn bị dữ liệu
        val checkIn = convertDate(checkInStr)
        val checkOut = convertDate(checkOutStr)
        val promoCode = etPromoCode.text.toString().trim().ifEmpty { null }

        // C. Tạo Request gửi lên Server
        val request = CreateBookingRequest(
            user_id = CurrentUser.id, // Lấy ID từ phiên đăng nhập (giả lập)
            room_ids = listOf(roomId),
            check_in = checkIn,
            check_out = checkOut,
            total_guests = 2,
            services = selectedServices,
            promotionCode = promoCode
        )

        // D. Gọi API
        RetrofitClient.instance.createBooking(request = request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Thành công -> Chuyển sang trang thông báo
                    val intent = Intent(this@CheckoutActivity, BookingSuccessfulActivity::class.java)
                    intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
                    intent.putExtra("TOTAL", tvTotal.text.toString())
                    startActivity(intent)
                    finish() // Đóng màn hình Checkout
                } else {
                    Toast.makeText(this@CheckoutActivity, "Đặt phòng thất bại: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Lỗi kết nối! Đang chuyển sang chế độ Demo...", Toast.LENGTH_SHORT).show()

                // [FALLBACK] Nếu mất mạng, vẫn cho qua màn hình thành công để Demo
                val intent = Intent(this@CheckoutActivity, BookingSuccessfulActivity::class.java)
                intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
                intent.putExtra("TOTAL", tvTotal.text.toString())
                startActivity(intent)
                finish()
            }
        })
    }

    // Helper: Chuyển đổi ngày dd-MM-yyyy -> yyyy-MM-dd
    private fun convertDate(dateStr: String): String {
        return try {
            val parts = dateStr.split("-")
            if (parts.size == 3) "${parts[2]}-${parts[1]}-${parts[0]}" else dateStr
        } catch (e: Exception) { dateStr }
    }

    // Helper: Ẩn bàn phím
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}