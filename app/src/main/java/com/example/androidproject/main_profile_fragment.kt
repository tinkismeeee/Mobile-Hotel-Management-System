package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [main_profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class main_profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val PersonalInformation = view.findViewById<LinearLayout>(R.id.btnEditInfo)
        PersonalInformation.setOnClickListener {
            val intent = Intent(requireActivity(), info_profile::class.java)
            startActivity(intent)
        }

        val logoutTextView = view.findViewById<TextView>(R.id.tvLogout)
        logoutTextView.setOnClickListener {
            val intent = Intent(requireActivity(), logout_profile::class.java)
            startActivity(intent)
        }

        val btnlanguages = view.findViewById<LinearLayout>(R.id.btnLanguages)
        btnlanguages.setOnClickListener {
            val intent = Intent(requireActivity(), languages_profile::class.java)
            startActivity(intent)
        }

        val btnnotifications = view.findViewById<LinearLayout>(R.id.btnNotifications)
        btnnotifications.setOnClickListener {
            val intent = Intent(requireActivity(), noti_profile::class.java)
            startActivity(intent)
        }

        val btnhelpAndSp = view.findViewById<LinearLayout>(R.id.btnhelpAndSp)
        btnhelpAndSp.setOnClickListener {
            val intent = Intent(requireActivity(), help_profile::class.java)
            startActivity(intent)
        }

        val btnsecurity = view.findViewById<LinearLayout>(R.id.btnSecurity)
        btnsecurity.setOnClickListener {
            val intent = Intent(requireActivity(), security_profile::class.java)
            startActivity(intent)
        }

        val btnyourcard = view.findViewById<LinearLayout>(R.id.btnYourcard)
        btnyourcard.setOnClickListener {
            val intent = Intent(requireActivity(), card_profile::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment main_profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            main_profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}