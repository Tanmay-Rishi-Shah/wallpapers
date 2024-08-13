package com.example.ovflats

import BaseActivity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class AdminDashboardActivity : BaseActivity() {

    private lateinit var logoutButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val towerA1: Button = findViewById(R.id.towerA1)
        val towerA2: Button = findViewById(R.id.towerA2)
        val towerA3: Button = findViewById(R.id.towerA3)
        val towerA4: Button = findViewById(R.id.towerA4)
        val towerA5: Button = findViewById(R.id.towerA5)
        val towerA6: Button = findViewById(R.id.towerA6)
        val towerA7: Button = findViewById(R.id.towerA7)



        towerA1.setOnClickListener {
            openFlatDetailsActivity("Tower A")
        }

        towerA2.setOnClickListener {
            openFlatDetailsActivity("Tower B")
        }

        towerA3.setOnClickListener {
            openFlatDetailsActivity("Tower C")
        }

        towerA4.setOnClickListener {
            openFlatDetailsActivity("Tower D")
        }

        towerA5.setOnClickListener {
            openFlatDetailsActivity("Tower E")
        }

        towerA6.setOnClickListener {
            openFlatDetailsActivity("Tower F")
        }

        towerA7.setOnClickListener {
            openFlatDetailsActivity("Tower G")
        }


        logoutButton = findViewById(R.id.logoutBtn)

        logoutButton.setOnClickListener {
            // Clear login state
            val sharedPreferences = getSharedPreferences("MyAdmin", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedInAdmin", false)
                apply()
            }
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun openFlatDetailsActivity(towerName: String) {
        val intent = Intent(this, AdminFlatDetailsActivity::class.java)
        intent.putExtra("TOWER_NAME", towerName)
        startActivity(intent)
    }
}