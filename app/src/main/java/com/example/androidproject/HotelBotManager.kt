package com.example.androidproject

import android.annotation.SuppressLint
import android.graphics.Color
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

    private val hotelInfo = "Bạn là lễ tân khách sạn. Trả lời ngắn gọn."

    fun setup() {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)

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

        try {
            activity.layoutInflater.inflate(R.layout.layout_hotel_bot, rootView, true)

            val cardPanel = rootView.findViewById<CardView>(R.id.cardChatPanel)
            val btnClose = rootView.findViewById<View>(R.id.btnCloseChat)
            val btnSend = rootView.findViewById<View>(R.id.btnChatSend)
            val etInput = rootView.findViewById<EditText>(R.id.etChatInput)
            val tvContent = rootView.findViewById<TextView>(R.id.tvChatContent)
            val scroll = rootView.findViewById<ScrollView>(R.id.scrollChat)

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
                    tvContent.append("\n\nBạn 🧑: $question")
                    etInput.text.clear()
                    scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
                    tvContent.append("\nBot 🤖: ...")

                    activity.lifecycleScope.launch {
                        try {
                            if (globalChatHistory == null) initAI()

                            val response = globalChatHistory?.sendMessage(question)
                            val text = tvContent.text.toString().replace("\nBot 🤖: ...", "")
                            tvContent.text = "$text\nBot 🤖: ${response?.text}"
                        } catch (e: Exception) {
                            tvContent.append("\n❌ Lỗi: ${e.message}")
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
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX; dY = v.y - event.rawY
                    startX = event.rawX; startY = event.rawY
                    startTime = System.currentTimeMillis()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate().x(event.rawX + dX).y(event.rawY + dY).setDuration(0).start()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (abs(event.rawX - startX) < 10 && (System.currentTimeMillis() - startTime) < 200) onClick()
                    true
                }
                else -> false
            }
        }
    }
}