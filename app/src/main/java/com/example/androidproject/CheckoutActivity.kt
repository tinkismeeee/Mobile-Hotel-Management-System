package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidproject.api.RetrofitClient
import com.example.androidproject.models.InvoicePreviewRequest
import com.example.androidproject.models.InvoiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class CheckoutActivity : AppCompatActivity() {

    // 1. Khai báo các View (Cũ + Mới)
    private lateinit var btnBack: ImageButton
    private lateinit var tvHotelName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvDates: TextView
    private lateinit var tvGuests: TextView
    private lateinit var btnConfirmPayment: Button

    // Các trường hiển thị giá
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvTotal: TextView

    // [MỚI] Các trường cho Mã giảm giá & Checkbox
    private lateinit var etPromoCode: EditText
    private lateinit var btnApplyPromo: Button
    private lateinit var tvDiscount: TextView
    private lateinit var rowDiscount: LinearLayout
    private lateinit var cbAgreement: CheckBox

    // Biến lưu dữ liệu
    private var roomId: Int = 1
    private var checkInDate: String = ""
    private var checkOutDate: String = ""

    // Công cụ định dạng tiền tệ (ví dụ: 1,000,000)
    private val currencyFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)

        // Xử lý giao diện tràn viền (Edge to Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Ánh xạ View (Tìm ID trong XML)
        btnBack = findViewById(R.id.btnBack)
        tvHotelName = findViewById(R.id.tvHotelName)
        tvLocation = findViewById(R.id.tvLocation)
        tvDates = findViewById(R.id.tvDates)
        tvGuests = findViewById(R.id.tvGuests)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)

        tvRoomPrice = findViewById(R.id.tvRoomPrice)
        tvTaxes = findViewById(R.id.tvTaxes)
        tvTotal = findViewById(R.id.tvTotal)

        // [MỚI] Ánh xạ các thành phần thêm vào
        etPromoCode = findViewById(R.id.etPromoCode)
        btnApplyPromo = findViewById(R.id.btnApplyPromo)
        tvDiscount = findViewById(R.id.tvDiscount)
        rowDiscount = findViewById(R.id.rowDiscount)
        cbAgreement = findViewById(R.id.cbAgreement)

        // 3. Nhận dữ liệu từ màn hình trước (RoomDetailActivity)
        val hotelName = intent.getStringExtra("HOTEL_NAME")
        val location = intent.getStringExtra("HOTEL_LOCATION")
        checkInDate = intent.getStringExtra("CHECK_IN_DATE") ?: "2025-12-24"
        checkOutDate = intent.getStringExtra("CHECK_OUT_DATE") ?: "2025-12-27"

        // Nhận ID phòng để tính tiền (Mặc định là 1 nếu lỗi)
        roomId = intent.getIntExtra("ROOM_ID", 1)

        // 4. Hiển thị thông tin cơ bản
        tvHotelName.text = hotelName
        tvLocation.text = location
        tvDates.text = "$checkInDate - $checkOutDate"
        tvGuests.text = "2 Guests" // Bạn có thể cập nhật logic số khách sau này

        // 5. Gọi API tính tiền lần đầu (Chưa có mã giảm giá)
        loadRealInvoiceData(roomId, checkInDate, checkOutDate, null)

        // Nút quay lại
        btnBack.setOnClickListener { finish() }

        // 6. [LOGIC MỚI] Xử lý nhập mã giảm giá
        btnApplyPromo.setOnClickListener {
            val code = etPromoCode.text.toString().trim()
            if (code.isNotEmpty()) {
                // Gọi lại API với mã giảm giá người dùng nhập
                loadRealInvoiceData(roomId, checkInDate, checkOutDate, code)

                // Ẩn bàn phím sau khi bấm
                val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            } else {
                Toast.makeText(this, "Vui lòng nhập mã giảm giá!", Toast.LENGTH_SHORT).show()
            }
        }

        // 7. [LOGIC MỚI] Xử lý Checkbox điều khoản
        // Ban đầu làm mờ nút thanh toán và không cho bấm
        btnConfirmPayment.alpha = 0.5f
        btnConfirmPayment.isEnabled = false

        cbAgreement.setOnCheckedChangeListener { _, isChecked ->
            // Nếu tích chọn -> Sáng nút lên và cho bấm. Ngược lại thì tắt.
            btnConfirmPayment.isEnabled = isChecked
            btnConfirmPayment.alpha = if (isChecked) 1.0f else 0.5f
        }

        // 8. Xử lý nút Thanh Toán (Chuyển sang màn hình thành công)
        btnConfirmPayment.setOnClickListener {
            val intent = Intent(this, BookingSuccessfulActivity::class.java)

            // Gửi ĐẦY ĐỦ thông tin sang màn hình kết quả
            intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
            intent.putExtra("DATES", tvDates.text.toString())
            intent.putExtra("GUESTS", tvGuests.text.toString())

            // Gửi chi tiết giá
            intent.putExtra("PRICE", tvRoomPrice.text.toString())
            intent.putExtra("TAX", tvTaxes.text.toString())
            intent.putExtra("TOTAL", tvTotal.text.toString())

            startActivity(intent)
        }
    }

    /**
     * Hàm gọi API để tính toán hóa đơn
     * @param promoCode: Mã giảm giá (có thể null nếu không nhập)
     */
    private fun loadRealInvoiceData(roomId: Int, checkIn: String, checkOut: String, promoCode: String?) {
        // Chuyển đổi ngày sang định dạng yyyy-MM-dd để gửi lên Server
        val safeCheckIn = convertDate(checkIn)
        val safeCheckOut = convertDate(checkOut)

        // Tạo gói tin yêu cầu (Request)
        val request = InvoicePreviewRequest(
            bookingId = null,
            roomIds = listOf(roomId),
            checkIn = safeCheckIn,
            checkOut = safeCheckOut,
            promoCode = promoCode // Gửi mã giảm giá (nếu có)
        )

        // Thực hiện gọi API
        RetrofitClient.instance.previewInvoice(request).enqueue(object : Callback<InvoiceResponse> {
            override fun onResponse(call: Call<InvoiceResponse>, response: Response<InvoiceResponse>) {
                if (response.isSuccessful) {
                    val invoice = response.body()
                    invoice?.let {
                        // Cập nhật giao diện với giá tiền thật từ Server
                        tvRoomPrice.text = "${currencyFormat.format(it.totalRoomCost)} VND"
                        tvTaxes.text = "${currencyFormat.format(it.vatAmount)} VND"
                        tvTotal.text = "${currencyFormat.format(it.finalTotal)} VND"

                        // [LOGIC MỚI] Kiểm tra xem có được giảm giá không
                        if (it.discountAmount > 0) {
                            rowDiscount.visibility = View.VISIBLE
                            tvDiscount.text = "- ${currencyFormat.format(it.discountAmount)} VND"
                            Toast.makeText(this@CheckoutActivity, "Áp dụng mã thành công!", Toast.LENGTH_SHORT).show()
                        } else {
                            // Không có giảm giá
                            rowDiscount.visibility = View.GONE

                            // Nếu người dùng CÓ nhập mã mà Server trả về 0đ giảm -> Mã sai
                            if (promoCode != null) {
                                Toast.makeText(this@CheckoutActivity, "Mã không hợp lệ hoặc không có giảm giá", Toast.LENGTH_SHORT).show()
                                etPromoCode.error = "Mã lỗi" // Báo đỏ ô nhập
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@CheckoutActivity, "Lỗi tính tiền: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<InvoiceResponse>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Hàm phụ trợ: Đổi ngày từ dd-MM-yyyy sang yyyy-MM-dd
    private fun convertDate(dateStr: String): String {
        return try {
            if (dateStr.contains("-") && dateStr.split("-")[0].length == 2) {
                val parts = dateStr.split("-")
                "${parts[2]}-${parts[1]}-${parts[0]}" // Đảo ngược lại
            } else {
                dateStr // Giữ nguyên nếu đã đúng
            }
        } catch (e: Exception) {
            "2025-12-24" // Giá trị mặc định an toàn
        }
    }
}