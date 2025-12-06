package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.facebook.CallbackManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import java.io.File
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    private lateinit var email_input: EditText
    private lateinit var password_input: EditText
    lateinit var callBackManager: CallbackManager

    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        try {
//            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
//            } else {
//                @Suppress("DEPRECATION")
//                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            }
//
//            // Lấy signatures an toàn
//            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                packageInfo.signingInfo?.apkContentsSigners ?: emptyArray()
//            } else {
//                @Suppress("DEPRECATION")
//                packageInfo.signatures ?: emptyArray()
//            }
//
//            for (signature in signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
//                Log.e("KEY_HASH", "KeyHash: $keyHash")
//            }
//
//        } catch (e: Exception) {
//            Log.e("KEY_HASH_ERROR", e.toString())
//        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        email_input = findViewById<EditText>(R.id.email_input)
        password_input = findViewById<EditText>(R.id.password_input)
        val signinBtn : Button = findViewById<Button>(R.id.signinBtn)
        val signupBtn : TextView = findViewById<TextView>(R.id.signupBtn)
        val incorrect_notification : TextView = findViewById<TextView>(R.id.incorrect_notification)
        val rememberBtn : RadioButton = findViewById<RadioButton>(R.id.rememberBtn)
        val password_visibility = findViewById<ImageView>(R.id.password_visibility)
        val mAuth = FirebaseAuth.getInstance()
        val facebook_login_Btn = findViewById<com.facebook.login.widget.LoginButton>(R.id.facebook_login_Btn)

        checkRemember()
        checkUserSession()

        val getResult = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_CANCELED) {
                val masterKey = MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
                val session = EncryptedSharedPreferences.create(
                    this,
                    getString(R.string.session_file_name),
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
                val isRemember = session.getBoolean("isRemember", false)
                if (isRemember) {
                    email_input.setText(session.getString("email", ""))
                }
                else {
                    Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
                }
            }
        }

        email_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                incorrect_notification.visibility = View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {
            override fun getTransformation(source: CharSequence, view: View): CharSequence {
                return PasswordCharSequence(source)
            }
            private inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {
                override fun get(index: Int): Char {
                    return '*'
                }
                override val length: Int
                    get() = source.length
                override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                    return source.subSequence(startIndex, endIndex)
                }
            }
        }
        password_input.transformationMethod = AsteriskPasswordTransformationMethod()

        password_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                incorrect_notification.visibility = View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        signinBtn.setOnClickListener {
            val email_inputStr : String? = email_input.text.toString()
            val password_inputStr : String? = password_input.text.toString()

            if (email_inputStr.isNullOrEmpty() || password_inputStr.isNullOrEmpty()) {
                incorrect_notification.visibility = View.VISIBLE
                email_input.text.clear()
                password_input.text.clear()
            }
            else if (email_inputStr == "admin" && password_inputStr == "admin") {
                Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
                val intent = Intent(this, admin::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else if (email_inputStr != null && password_inputStr != null) {
//                Log.i("FirebaseAuthCheck", email_inputStr.toString())
                mAuth.signInWithEmailAndPassword(email_inputStr.toString(), password_inputStr.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val masterKey = MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
                        val session = EncryptedSharedPreferences.create(
                            this,
                            getString(R.string.session_file_name),
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )
                        val mySession = session.edit()
                        mySession.putString("email", email_inputStr)
                        mySession.putString("password", password_inputStr)
                        mySession.putBoolean("isLoggedIn", true)
                        mySession.putBoolean("isRemember", rememberBtn.isChecked)
                        //                Log.i("remember", "checked")
                        mySession.apply()

                        val intent = Intent(this, enterOTP::class.java)
                        Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
                        getResult.launch(intent)
                    }
                    else {
                        incorrect_notification.visibility = View.VISIBLE
                        Log.i("FirebaseAuthCheck", task.exception.toString())
                    }
                }
            }
        }

        signupBtn.setOnClickListener {
            val intent : Intent = Intent(this, signup::class.java)
            Reset_all(email_input, password_input, incorrect_notification, rememberBtn)
            startActivity(intent)
        }

        password_visibility.setOnClickListener {
            if (password_input.transformationMethod == null) {
                password_input.transformationMethod = AsteriskPasswordTransformationMethod()
            }
            else {
                password_input.transformationMethod = null
            }
        }

        // Đăng nhập bằng facebook
        callBackManager = CallbackManager.Factory.create()
        facebook_login_Btn.setReadPermissions("email", "public_profile")
        facebook_login_Btn.registerCallback(callBackManager, object : com.facebook.FacebookCallback<com.facebook.login.LoginResult> {
            override fun onSuccess(loginResult: com.facebook.login.LoginResult) {
                Log.d("Facebook", "onSuccess: Facebook login successful.")
                handleFacebookAccessToken(loginResult.accessToken)
            }
            override fun onCancel() {
                Log.d("Facebook", "onCancel: Facebook login canceled by user.")
                Toast.makeText(this@MainActivity, "Login with Facebook canceled", Toast.LENGTH_SHORT).show()
            }
            override fun onError(error: com.facebook.FacebookException) {
                Log.e("Facebook", "onError: ${error.message}")
                Toast.makeText(this@MainActivity, "Login with Facebook failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleFacebookAccessToken(token: com.facebook.AccessToken) {
        val credential = com.google.firebase.auth.FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                Log.d("Facebook", "signInWithCredential: success")
                Toast.makeText(this, "Login with Facebook successful.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Log.w("Facebook", "signInWithCredential: failure", task.exception)
                Toast.makeText(this, "Login with Facebook failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun Reset_all (email_input: EditText, password_input: EditText, incorrect_notification: TextView, rememberBtn: RadioButton) {
        email_input.text.clear()
        password_input.text.clear()
        incorrect_notification.visibility = View.GONE
        rememberBtn.isChecked = false
    }

    private fun checkUserSession() {
        // Login Session
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val session = EncryptedSharedPreferences.create(
            this,
            getString(R.string.session_file_name),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        val isLoggedIn = session.getBoolean("isLoggedIn", false)
        if (isLoggedIn == true) {
            Log.i("Login Session", "User is logged in")
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
        else {
            Log.e("Login Session", "User is not logged in")
        }
    }
    private fun checkRemember() {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val session = EncryptedSharedPreferences.create(
            this,
            getString(R.string.session_file_name),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        val isChecked = session.getBoolean("isRemember", false)
        if (isChecked == true) {
            email_input.setText(session.getString("email", ""))
            password_input.text.clear()
        }
    }
}