package com.example.androidproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import kotlin.math.abs

class HotelBotManager(
    private val activity: AppCompatActivity,
    private val apiKey: String
) {
    companion object {
        private var globalChatHistory: Chat? = null
    }

    // --- KỊCH BẢN MỚI: Dạy Bot trả về LỆNH ---
    private val hotelInfo = """
        Bạn là AI đặt phòng khách sạn thông minh.
        
        NHIỆM VỤ QUAN TRỌNG:
        Khi khách muốn tìm phòng, hỏi phòng trống, hoặc đưa ra số người/ngân sách.
        TUYỆT ĐỐI KHÔNG trả lời văn bản mô tả phòng.
        HÃY TRẢ VỀ LỆNH DUY NHẤT theo mẫu: [[SEARCH:số_người:giá_tối_đa]]
        
        Quy tắc:
        - số_người: Mặc định là 1 nếu không nói.
        - giá_tối_đa: Mặc định là -1 nếu không nói.
        
        Ví dụ mẫu:
        - Khách: "Tìm phòng 2 người" -> Trả lời: [[SEARCH:2:-1]]
        - Khách: "Có phòng nào dưới 500k không?" -> Trả lời: [[SEARCH:1:500000]]
        - Khách: "Phòng 4 người giá 1 triệu" -> Trả lời: [[SEARCH:4:1000000]]
        
        Với các câu hỏi khác (Wifi, Checkin), trả lời ngắn gọn tiếng Việt.
    """.trimIndent()

    fun setup() {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)

        // Tạo nút tròn
        val btnFloatingBot = ImageView(activity).apply {
            setImageResource(android.R.drawable.ic_dialog_email)
            setBackgroundColor(Color.parseColor("#1976D2"))
            setPadding(30, 30, 30, 30)
            background = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(Color.parseColor("#1976D2"))
                setStroke(4, Color.WHITE)
            }
            elevation = 100f
        }
        val params = FrameLayout.LayoutParams(160, 160).apply {
            gravity = Gravity.BOTTOM or Gravity.END
            setMargins(0, 0, 40, 200)
        }
        rootView.addView(btnFloatingBot, params)

        // Nạp giao diện chat
        try {
            activity.layoutInflater.inflate(R.layout.layout_hotel_bot, rootView, true)
            val cardPanel = rootView.findViewById<CardView>(R.id.cardChatPanel)
            val btnClose = rootView.findViewById<View>(R.id.btnCloseChat)
            val btnSend = rootView.findViewById<View>(R.id.btnChatSend)
            val etInput = rootView.findViewById<EditText>(R.id.etChatInput)
            val tvContent = rootView.findViewById<TextView>(R.id.tvChatContent)
            val scroll = rootView.findViewById<ScrollView>(R.id.scrollChat)

            // Cho phép bấm vào link trong TextView
            tvContent.movementMethod = LinkMovementMethod.getInstance()

            rootView.findViewById<View>(R.id.btnFloatingBot)?.visibility = View.GONE
            cardPanel.bringToFront()
            cardPanel.elevation = 101f

            btnClose.setOnClickListener {
                cardPanel.visibility = View.GONE
                btnFloatingBot.visibility = View.VISIBLE
            }

            btnSend.setOnClickListener {
                val question = etInput.text.toString()
                if (question.isNotEmpty()) {
                    appendMessage(tvContent, "\n\n🧑: $question")
                    etInput.text.clear()
                    scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
                    appendMessage(tvContent, "\n🤖: ...")

                    activity.lifecycleScope.launch {
                        try {
                            if (globalChatHistory == null) initAI()

                            val response = globalChatHistory?.sendMessage(question)
                            val botReply = response?.text?.trim() ?: ""

                            //Xóa "..."
                            val currentText = tvContent.text.toString().replace("\n🤖: ...", "")
                            tvContent.text = currentText

                            //LOGIC XỬ LÝ LỆNH TÌM KIẾM
                            if (botReply.startsWith("[[SEARCH:")) {
                                //1. Tách lệnh lấy thông số
                                //Ví dụ: [[SEARCH:2:500000]] -> guests=2, price=500000
                                val cleanCmd = botReply.removePrefix("[[SEARCH:").removeSuffix("]]")
                                val params = cleanCmd.split(":")
                                val guests = params.getOrNull(0)?.toIntOrNull() ?: 1
                                val maxPrice = params.getOrNull(1)?.toDoubleOrNull() ?: -1.0

                                //2. Gọi hàm tìm phòng từ file RoomRepository
                                val foundRooms = RoomRepository.findRooms(guests, maxPrice)

                                if (foundRooms.isEmpty()) {
                                    appendMessage(tvContent, "\n🤖: Rất tiếc, không tìm thấy phòng phù hợp ạ.")
                                } else {
                                    appendMessage(tvContent, "\n🤖: Tìm thấy ${foundRooms.size} phòng cho bạn:\n")

                                    //3. Tạo danh sách hiển thị đẹp (HTML)
                                    for (room in foundRooms) {
                                        val htmlInfo = """
                                            <br><b>🏨 ${room.name}</b><br>
                                            👤 ${room.capacity} người - 💵 ${formatMoney(room.price)}<br>
                                            <i>${room.description}</i><br>
                                            <a href="book:${room.id}">👉 <b>BẤM ĐỂ ĐẶT NGAY</b></a><br>
                                            -------------------
                                        """.trimIndent()
                                        //Gọi hàm hiển thị HTML đặc biệt
                                        appendHtmlMessage(tvContent, htmlInfo)
                                    }
                                }
                            } else {
                                //Nếu không phải lệnh tìm kiếm thì hiện tin nhắn thường
                                appendMessage(tvContent, "\n🤖: $botReply")
                            }
                        } catch (e: Exception) {
                            appendMessage(tvContent, "\n❌: ${e.localizedMessage}")
                        }
                        scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
                    }
                }
            }

            setupDragAndDrop(btnFloatingBot) {
                cardPanel.visibility = View.VISIBLE
                btnFloatingBot.visibility = View.GONE
                cardPanel.alpha = 0f
                cardPanel.animate().alpha(1f).setDuration(300).start()
            }
        } catch (e: Exception) {}

        if (globalChatHistory == null) initAI()
    }

    //Hàm hiện text thường
    private fun appendMessage(textView: TextView, text: String) {
        val current = textView.text.toString()
        textView.text = current + text
    }

    //Hàm hiện HTML và Xử lý sự kiện bấm vào Link
    private fun appendHtmlMessage(textView: TextView, html: String) {
        val currentText = textView.text
        //Chuyển mã HTML thành văn bản hiển thị được
        val newText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        //Nối vào nội dung cũ
        val builder = SpannableString(currentText).toString() + newText
        textView.append(newText)

        //Quét tìm link để gán sự kiện Click
        handleCustomLinks(textView)
    }

    private fun handleCustomLinks(textView: TextView) {
        val text = textView.text as? android.text.Spannable ?: return
        val urls = text.getSpans(0, text.length, android.text.style.URLSpan::class.java)

        for (span in urls) {
            val url = span.url
            // Nếu link bắt đầu bằng "book:" (Do ta tự quy định ở trên)
            if (url.startsWith("book:")) {
                val start = text.getSpanStart(span)
                val end = text.getSpanEnd(span)

                // Xóa link mặc định, thay bằng hành động mở màn hình
                text.removeSpan(span)
                text.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val roomId = url.removePrefix("book:")
                        // Gọi hàm xử lý đặt phòng
                        goToBookingScreen(roomId)
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    // Hàm chuyển màn hình khi bấm "ĐẶT NGAY"
    private fun goToBookingScreen(roomId: String) {
        val room = RoomRepository.getRoomById(roomId)
        if (room != null) {
            Toast.makeText(activity, "Đang chọn phòng: ${room.name}", Toast.LENGTH_SHORT).show()

            // to BookingSuccessfulActivity
            val intent = Intent(activity, BookingSuccessfulActivity::class.java)
            // Gửi kèm dữ liệu sang màn hình kia
            intent.putExtra("HOTEL_NAME", room.name)
            intent.putExtra("PRICE", formatMoney(room.price))
            intent.putExtra("GUESTS", "${room.capacity} người")
            activity.startActivity(intent)
        }
    }

    private fun formatMoney(amount: Double) = String.format("%,.0f đ", amount)

    private fun initAI() {
        try {
            val model = GenerativeModel(
                modelName = "gemini-flash-latest",
                apiKey = apiKey,
                systemInstruction = content { text(hotelInfo) }
            )
            globalChatHistory = model.startChat()
        } catch (e: Exception) {}
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDragAndDrop(view: View, onClick: () -> Unit) {
        var dX = 0f; var dY = 0f; var startX = 0f; var startY = 0f; var startTime: Long = 0
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { dX = v.x - event.rawX; dY = v.y - event.rawY; startX = event.rawX; startY = event.rawY; startTime = System.currentTimeMillis(); true }
                MotionEvent.ACTION_MOVE -> { v.animate().x(event.rawX + dX).y(event.rawY + dY).setDuration(0).start() ; true }
                MotionEvent.ACTION_UP -> { if (abs(event.rawX - startX) < 10 && (System.currentTimeMillis() - startTime) < 200) onClick(); true }
                else -> false
            }
        }
    }
}