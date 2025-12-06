package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.CallbackManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class signup : AppCompatActivity() {
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var callBackManager: CallbackManager
    @Override
    protected fun onStart(savedInstanceState: Bundle?) {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent : Intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val backBtn : ImageView = findViewById<ImageView>(R.id.backBtn)
        val name_input : EditText = findViewById<EditText>(R.id.name_input)
        val email_input : EditText = findViewById<EditText>(R.id.email_input)
        val password_input : EditText = findViewById<EditText>(R.id.password_input)
        val password_visibility: ImageView = findViewById<ImageView>(R.id.password_visibility)
        val createAccountBtn: Button = findViewById<Button>(R.id.continueBtn)
        val username_input : EditText = findViewById<EditText>(R.id.username_input)
        val address_input : EditText = findViewById<EditText>(R.id.address_input)
        val phone_input : EditText = findViewById<EditText>(R.id.phone_input)
        val facebook_login_btn: ImageView = findViewById<ImageView>(R.id.facebook_login)

        //AsteriskPasswordTransformationMethod
        class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {
            override fun getTransformation(source: CharSequence, view: View): CharSequence {
                return PasswordCharSequence(source)
            }
            private inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {
                override fun get(index: Int): Char {
                    return '*' // This is the important part
                }
                override val length: Int
                    get() = source.length // Return default
                override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                    return source.subSequence(startIndex, endIndex) // Return default
                }
            }
        }
        password_input.transformationMethod = AsteriskPasswordTransformationMethod()
        password_visibility.setOnClickListener {
            if (password_input.transformationMethod == null) {
                password_input.transformationMethod = AsteriskPasswordTransformationMethod()
            }
            else {
                password_input.transformationMethod = null
            }
        }

        // Add user to Firebase Authentication
        createAccountBtn.setOnClickListener {
            val password_inputStr: String = password_input.text.toString()
            val name_inputStr: String = name_input.text.toString()
            val email_inputStr: String = email_input.text.toString()
            val username_inputStr: String = username_input.text.toString()
            val address_inputStr: String = address_input.text.toString()
            val phone_inputStr: String = phone_input.text.toString()

            if (name_inputStr.isNotEmpty() && email_inputStr.isNotEmpty() && password_inputStr.isNotEmpty() && username_inputStr.isNotEmpty() && address_inputStr.isNotEmpty() && phone_inputStr.isNotEmpty()) {
                mAuth.createUserWithEmailAndPassword(email_inputStr, password_inputStr).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            Log.d("AUTH", "createUserWithEmail: success")
                            val user = mAuth.getCurrentUser()

                            val nameParts = name_inputStr.trim().split(" ")
                            val firstName = nameParts[0]
                            val lastName = if (nameParts.size > 1) nameParts[1] else ""
                            val newCustomer = NewCustomer(
                                username = username_inputStr,
                                password = password_inputStr,
                                email = email_inputStr,
                                first_name = firstName,
                                last_name = lastName,
                                phone_number = phone_inputStr,
                                address = address_inputStr,
                                date_of_birth = null,
                                is_active = true
                            )

                            RetrofitClient.instance.addCustomer(newCustomer)
                                .enqueue(object : Callback<NewCustomer> {
                                    override fun onResponse(call: Call<NewCustomer>, response: Response<NewCustomer>) {
                                        if (response.isSuccessful) {
                                            Log.d("API", "Customer added successfully")
                                            Toast.makeText(getApplicationContext(), "Signup successful", Toast.LENGTH_LONG).show();
                                            val intent = Intent(this@signup, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Log.e("API", "API Error: ${response.code()} - ${response.message()}")
                                            mAuth.currentUser?.delete()
                                        }
                                    }
                                    override fun onFailure(call: Call<NewCustomer>, t: Throwable) {
                                        Log.e("API", "API Failure: ${t.message}")
                                        Toast.makeText(this@signup, "Lỗi kết nối tới server: ${t.message}", Toast.LENGTH_LONG).show()
                                        mAuth.currentUser?.delete()
                                    }
                                })
                        }
                        else {
                            Log.w("AUTH", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@signup, "Authentication failed. Account is already registered.",Toast.LENGTH_SHORT).show()
                            Reset_all(name_input, email_input, password_input, username_input, address_input, phone_input)
                        }
                    }
                })
            }
            else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn.setOnClickListener {
            Reset_all(name_input, email_input, password_input, username_input, address_input, phone_input)
            finish()
        }
    }

    fun Reset_all(name_input: EditText, email_input: EditText, password_input: EditText, username_input: EditText, address_input: EditText, phone_input: EditText) {
        name_input.text.clear()
        email_input.text.clear()
        password_input.text.clear()
        username_input.text.clear()
        address_input.text.clear()
        phone_input.text.clear()
    }

}