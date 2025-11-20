package com.example.androidproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    // Dùng TAG này để gọi BottomSheet
    companion object {
        const val TAG = "FilterBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout cho fragment
        return inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tìm nút Apply
        val btnApply: MaterialButton = view.findViewById(R.id.btn_apply_filter)

        // Xử lý click
        btnApply.setOnClickListener {
            // TODO: Lấy giá trị của các CheckBox và gửi về Activity
            Toast.makeText(context, "Đã áp dụng bộ lọc!", Toast.LENGTH_SHORT).show()

            // Đóng BottomSheet
            dismiss()
        }
    }
}