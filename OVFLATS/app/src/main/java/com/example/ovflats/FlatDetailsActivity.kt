package com.example.ovflats

import BaseActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FlatDetailsActivity : BaseActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var towerName: String
    private lateinit var progressBar: ProgressBar
    private lateinit var flatDetailsContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flat_details)

        val towerNameTextView: TextView = findViewById(R.id.towerNameTextView)
        progressBar = findViewById(R.id.progressBar)
        flatDetailsContainer = findViewById(R.id.flatDetailsContainer)

        towerName = intent.getStringExtra("TOWER_NAME") ?: "Unknown Tower"
        towerNameTextView.text = towerName

        firestore = FirebaseFirestore.getInstance()

        fetchAndDisplayFlatDetails()
    }

    private fun fetchAndDisplayFlatDetails() {
        progressBar.visibility = View.VISIBLE
        firestore.collection("towers").document(towerName).collection("floors")
            .get()
            .addOnSuccessListener { floorDocuments ->
                flatDetailsContainer.removeAllViews()
                val sortedFloors = floorDocuments.documents.sortedBy { it.id }
                for (floorDoc in sortedFloors) {
                    floorDoc.reference.collection("flats").get().addOnSuccessListener { flatDocuments ->
                        displayFlats(flatDocuments)
                    }.addOnFailureListener {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Failed to load flats", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load floors", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayFlats(flatDocuments: QuerySnapshot) {
        for (flatDoc in flatDocuments.documents) {
            val flatNumber = flatDoc.id
            val isOccupiedA = flatDoc.getBoolean("A") ?: false
            val isOccupiedB = flatDoc.getBoolean("B") ?: false

            val flatContainer = LinearLayout(this)
            flatContainer.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            flatContainer.orientation = LinearLayout.VERTICAL
            flatContainer.gravity = Gravity.CENTER_VERTICAL
            flatContainer.setPadding(16, 8, 16, 8)

            val flatHeader = TextView(this)
            flatHeader.text = flatNumber // Showing flat number only
            flatHeader.textSize = 18f
            flatHeader.setPadding(8, 8, 8, 8)
            flatContainer.addView(flatHeader)

            val flatStatusContainer = LinearLayout(this)
            flatStatusContainer.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            flatStatusContainer.orientation = LinearLayout.HORIZONTAL
            flatStatusContainer.gravity = Gravity.CENTER_VERTICAL
            flatStatusContainer.setPadding(16, 8, 16, 8)

            val flatATextView = TextView(this)
            val paramsA = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            flatATextView.layoutParams = paramsA
            flatATextView.text = "A: ${if (isOccupiedA) "Occupied" else "Vacant"}"
            flatATextView.textSize = 18f
            flatATextView.gravity = Gravity.CENTER
            flatATextView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    if (isOccupiedA) android.R.color.holo_green_light
                    else android.R.color.holo_red_light
                )
            )
            flatATextView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            flatATextView.setPadding(16, 8, 16, 8)
            flatStatusContainer.addView(flatATextView)

            val flatBTextView = TextView(this)
            val paramsB = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            flatBTextView.layoutParams = paramsB
            flatBTextView.text = "B: ${if (isOccupiedB) "Occupied" else "Vacant"}"
            flatBTextView.textSize = 18f
            flatBTextView.gravity = Gravity.CENTER
            flatBTextView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    if (isOccupiedB) android.R.color.holo_green_light
                    else android.R.color.holo_red_light
                )
            )
            flatBTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            flatBTextView.setPadding(16, 8, 16, 8)
            flatStatusContainer.addView(flatBTextView)

            flatContainer.addView(flatStatusContainer)
            flatDetailsContainer.addView(flatContainer)
        }
        progressBar.visibility = View.GONE
    }
}