package com.example.ovflats

import BaseActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.text.InputType
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class UserActivity : BaseActivity() {
    private lateinit var db: FirebaseFirestore
    private var isPasswordVisible = false

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        db = FirebaseFirestore.getInstance()

        val notAdminButton = findViewById<TextView>(R.id.notUser)
        val requestPasswordButton = findViewById<TextView>(R.id.requestPassword)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginBtn)

        val eyeIcon = findViewById<ImageView>(R.id.eyeIcon)

        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeIcon.setImageResource(R.drawable.colsedeye) // Change the icon to indicate hiding password
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye) // Change the icon to indicate showing password
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Check the credentials in Firestore
            db.collection("users")
                .whereEqualTo("user_username", username)
                .whereEqualTo("user_password", password)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    } else {
                        // Store login state
                        val sharedPreferences = getSharedPreferences("MyUser", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedInUser", true)
                            apply()
                        }
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, UserDashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }


        notAdminButton.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }

        requestPasswordButton.setOnClickListener {
            openWhatsApp("+917838349247", "Hey Sir I am User of OV Flats\nI forgot my username and password\nPlease Provide my details...")
        }

    }

    private fun openWhatsApp(phoneNumber: String, message: String) {
        try {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception if WhatsApp is not installed
        }
    }
}