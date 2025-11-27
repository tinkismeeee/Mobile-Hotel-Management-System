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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class signup : AppCompatActivity() {
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

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
            if (name_inputStr.isNotEmpty() && email_inputStr.isNotEmpty() && password_inputStr.isNotEmpty()) {
                mAuth.createUserWithEmailAndPassword(email_inputStr, password_inputStr).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            Log.d("AUTH", "createUserWithEmail:success")
                            val user = mAuth.getCurrentUser()
                            Toast.makeText(getApplicationContext(), user?.getEmail(), Toast.LENGTH_LONG).show();
                            val intent: Intent = Intent(this@signup, Home::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            Log.w("AUTH", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@signup, "Authentication failed.",Toast.LENGTH_SHORT).show()
                            Reset_all(name_input, email_input, password_input)
                        }
                    }
                })
            }
            else {
                Log.i("AUTH", "Empty fields")
            }
        }

        backBtn.setOnClickListener {
            Reset_all(name_input, email_input, password_input)
            finish()
        }
    }

    fun Reset_all(name_input: EditText, email_input: EditText, password_input: EditText) {
        name_input.text.clear()
        email_input.text.clear()
        password_input.text.clear()
    }
}