package com.example.androidproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
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
import kotlin.apply
import kotlin.collections.getOrNull
import kotlin.jvm.java
import kotlin.math.abs
import kotlin.text.format
import kotlin.text.isNotEmpty
import kotlin.text.removePrefix
import kotlin.text.removeSuffix
import kotlin.text.replace
import kotlin.text.split
import kotlin.text.startsWith
import kotlin.text.toDoubleOrNull
import kotlin.text.toIntOrNull
import kotlin.text.trim
import kotlin.text.trimIndent

class HotelBotManager(
    private val activity: AppCompatActivity,
    private val apiKey: String
) {
    companion object {
        private var globalChatHistory: Chat? = null
    }
    private val hotelInfo = """
    Bạn là AI đặt phòng khách sạn thông minh. 
    NHIỆM VỤ CỦA BẠN:
    → KHÔNG trả lời mô tả phòng.
    → CHỈ TRẢ VỀ CÁC LỆNH SAU:
        [[SEARCH:số_người:giá_tối_đa]]
        [[LOWEST]]
        [[HIGHEST]]

    ----------------------------------
    QUY TẮC HIỂU NGÔN NGỮ NGƯỜI DÙNG
    ----------------------------------

    1️⃣ Khi khách yêu cầu theo SỐ NGƯỜI:
       - Ví dụ:
         "Tìm phòng 2 người" → [[SEARCH:2:-1]]
         "Phòng cho 3 người" → [[SEARCH:3:-1]]
       → Nếu không nhắc đến giá → giá_tối_đa = -1 (không giới hạn)

    2️⃣ Khi khách yêu cầu theo GIÁ:
       - "giá dưới X"
       - "nhỏ hơn X"
       - "không quá X"
       - "tối đa X"
       → giá_tối_đa = X

       Ví dụ:
       "Có phòng nào dưới 500k không?" → [[SEARCH:1:500000]]
       (mặc định số người = 1 nếu không nói)

    3️⃣ Khi khách yêu cầu cả SỐ NGƯỜI + GIÁ:
       - "Tìm phòng X người giá dưới Y"
       - "Phòng X người tối đa Y"
       → TRẢ VỀ: [[SEARCH:X:Y]]

       Ví dụ:
       "Tìm phòng 1 người giá dưới 1000000" → [[SEARCH:1:1000000]]
       "Phòng 2 người dưới 600k" → [[SEARCH:2:600000]]

    4️⃣ Khi khách yêu cầu phòng RẺ NHẤT:
       - "Cho tôi phòng rẻ nhất"
       - "Phòng nào giá thấp nhất"
       → [[LOWEST]]

    5️⃣ Khi khách yêu cầu phòng ĐẮT NHẤT:
       - "Phòng đắt nhất là phòng nào"
       - "Cho tôi phòng cao cấp nhất"
       → [[HIGHEST]]

    ----------------------------------
    VÍ DỤ MẪU (BẮT BUỘC LÀM THEO)
    ----------------------------------
    - "Tìm phòng 2 người" → [[SEARCH:2:-1]]
    - "Tìm phòng giá dưới 700k" → [[SEARCH:1:700000]]
    - "Phòng 4 người giá 1 triệu" → [[SEARCH:4:1000000]]
    - "Có phòng nào dưới 500k không?" → [[SEARCH:1:500000]]
    - "Cho tôi phòng rẻ nhất" → [[LOWEST]]
    - "Cho tôi phòng đắt nhất" → [[HIGHEST]]
    - "Tìm phòng 1 người giá dưới 1000000" → [[SEARCH:1:1000000]]

    ----------------------------------
    LUẬT CUỐI:
    - KHÔNG TRẢ LỜI THÊM.
    - KHÔNG GIẢI THÍCH.
    - KHÔNG VIẾT KÈM CHỮ.
    - CHỈ TRẢ VỀ LỆNH DẠNG [[...]]
""".trimIndent()


    fun setup() {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)

        val btnFloatingBot = ImageView(activity).apply {
            setImageResource(android.R.drawable.ic_dialog_email)
            setBackgroundColor(Color.parseColor("#1976D2"))
            setPadding(30, 30, 30, 30)
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
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

            tvContent.movementMethod = LinkMovementMethod.getInstance()

            btnFloatingBot.visibility = View.GONE
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
                                            <br>
                                            -----------------------------------------
                                        """.trimIndent()
                                        appendHtmlMessage(tvContent, htmlInfo)
                                    }
                                }
                            }
                            else if (botReply == "[[LOWEST]]") {
                                val room = RoomRepository.getLowestPriceRoom()

                                if (room != null) {
                                    val htmlInfo = """
                                        <br><b>🏨 ${room.name}</b><br>
                                        👤 ${room.capacity} người - 💵 ${formatMoney(room.price)}<br>
                                        <i>${room.description}</i><br>
                                        <br>
                                        -----------------------------------------
                                        """.trimIndent()
                                    appendHtmlMessage(tvContent, htmlInfo)
                                } else {
                                    appendMessage(tvContent, "\n🤖: Không có phòng nào cả.")
                                }
                            }
                            else if (botReply == "[[HIGHEST]]") {
                                val room = RoomRepository.getHighestPriceRoom()

                                if (room != null) {
                                    val htmlInfo = """
                                    <br><b>🏨 ${room.name}</b><br>
                                    👤 ${room.capacity} người - 💵 ${formatMoney(room.price)}<br>
                                    <i>${room.description}</i><br>
                                    <br>
                                    -----------------------------------------
                                    """.trimIndent()
                                    appendHtmlMessage(tvContent, htmlInfo)
                                } else {
                                    appendMessage(tvContent, "\n🤖: Không có phòng nào cả.")
                                }
                            }
                            else {
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

    private fun appendMessage(textView: TextView, text: String) {
        val current = textView.text.toString()
        textView.text = current + text
    }

    private fun appendHtmlMessage(textView: TextView, html: String) {
        val currentText = textView.text
        val newText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        val builder = SpannableString(currentText).toString() + newText
        textView.append(newText)

        handleCustomLinks(textView)
    }

    private fun handleCustomLinks(textView: TextView) {
        val text = textView.text as? Spannable ?: return
        val urls = text.getSpans(0, text.length, URLSpan::class.java)

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
//                        goToBookingScreen(roomId)
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

//    private fun goToBookingScreen(roomId: String) {
//        val room = RoomRepository.getRoomById(roomId)
//        if (room != null) {
//            Toast.makeText(activity, "Đang chọn phòng: ${room.name}", Toast.LENGTH_SHORT).show()
//
//            val intent = Intent(activity, BookingSuccessfulActivity::class.java)
//            intent.putExtra("HOTEL_NAME", room.name)
//            intent.putExtra("PRICE", formatMoney(room.price))
//            intent.putExtra("GUESTS", "${room.capacity} người")
//            activity.startActivity(intent)
//        }
//    }

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