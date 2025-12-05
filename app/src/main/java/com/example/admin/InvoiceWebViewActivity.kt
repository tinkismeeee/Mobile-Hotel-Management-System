package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class InvoiceWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_web_view)

        val webView = findViewById<WebView>(R.id.webView)
        val btnToggleLayout = findViewById<ImageView>(R.id.btnToggleLayout)
        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            startActivity(Intent(this, DoanhThuActivity::class.java))
            finish()
        }


        val invoiceJson = intent.getStringExtra("INVOICE_JSON")
        val invoice = JSONObject(invoiceJson ?: "{}")

        val html = """
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <style>
                    body { font-family: Arial, sans-serif; margin:0; padding:16px; background:#f0f0f5; }
                    .container { background:#fff; padding:24px; border-radius:12px; box-shadow:0 4px 15px rgba(0,0,0,0.1); max-width:600px; margin:auto; }
                    .row { margin-bottom:16px; font-size:16px; opacity:0; transform: translateY(20px); animation: fadeIn 0.5s forwards; }
                    .row:nth-child(1) { animation-delay: 0.1s; }
                    .row:nth-child(2) { animation-delay: 0.2s; }
                    .row:nth-child(3) { animation-delay: 0.3s; }
                    .row:nth-child(4) { animation-delay: 0.4s; }
                    .row:nth-child(5) { animation-delay: 0.5s; }
                    .row:nth-child(6) { animation-delay: 0.6s; }
                    .row:nth-child(7) { animation-delay: 0.7s; }
                    .row:nth-child(8) { animation-delay: 0.8s; }
                    .row:nth-child(9) { animation-delay: 0.9s; }
                    .row:nth-child(10) { animation-delay: 1s; }
                    .row:nth-child(11) { animation-delay: 1.1s; }
                    .row:nth-child(12) { animation-delay: 1.2s; }
                    .label { font-weight:bold; color:#333; }
                    .value { color:#555; margin-left:6px; }
                    @keyframes fadeIn { to { opacity:1; transform: translateY(0); } }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="row"><span class="label">Invoice ID:</span><span class="value">${invoice.optInt("invoice_id")}</span></div>
                    <div class="row"><span class="label">Booking ID:</span><span class="value">${invoice.optInt("booking_id")}</span></div>
                    <div class="row"><span class="label">Staff ID:</span><span class="value">${invoice.optInt("staff_id")}</span></div>
                    <div class="row"><span class="label">Issue Date:</span><span class="value">${invoice.optString("issue_date")}</span></div>
                    <div class="row"><span class="label">Total Room Cost:</span><span class="value">${invoice.optString("total_room_cost")}</span></div>
                    <div class="row"><span class="label">Total Service Cost:</span><span class="value">${invoice.optString("total_service_cost")}</span></div>
                    <div class="row"><span class="label">Discount Amount:</span><span class="value">${invoice.optString("discount_amount")}</span></div>
                    <div class="row"><span class="label">Final Amount:</span><span class="value">${invoice.optString("final_amount")}</span></div>
                    <div class="row"><span class="label">VAT Amount:</span><span class="value">${invoice.optString("vat_amount")}</span></div>
                    <div class="row"><span class="label">Payment Method:</span><span class="value">${invoice.optString("payment_method")}</span></div>
                    <div class="row"><span class="label">Promotion ID:</span><span class="value">${invoice.optString("promotion_id")}</span></div>
                    <div class="row"><span class="label">Payment Status:</span><span class="value">${invoice.optString("payment_status")}</span></div>
                </div>
            </body>
            </html>
        """.trimIndent()

        webView.settings.javaScriptEnabled = true
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
    }
}
