package com.example.doan1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main6)
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        // Gọi hàm gán sự kiện cho từng mục FAQ
        setupFAQ(R.id.faq1Header, R.id.faq1Content, R.id.faq1Arrow)
        setupFAQ(R.id.faq2Header, R.id.faq2Content, R.id.faq2Arrow)
        setupFAQ(R.id.faq3Header, R.id.faq3Content, R.id.faq3Arrow)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupFAQ(headerId: Int, contentId: Int, arrowId: Int) {
        val header = findViewById<LinearLayout>(headerId)
        val content = findViewById<TextView>(contentId)
        val arrow = findViewById<ImageView>(arrowId)

        header.setOnClickListener {
            if (content.visibility == View.GONE) {
                content.visibility = View.VISIBLE
                arrow.setImageResource(R.drawable.ic_expand_less)
            } else {
                content.visibility = View.GONE
                arrow.setImageResource(R.drawable.ic_expand_more)
            }
        }

    }

}