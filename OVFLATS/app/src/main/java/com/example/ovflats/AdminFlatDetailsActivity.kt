package com.example.ovflats

import BaseActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class AdminFlatDetailsActivity : BaseActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var towerName: String
    private lateinit var progressBar: ProgressBar
    private lateinit var flatDetailsContainer: LinearLayout
    private lateinit var updateButton: Button
    private val floorUpdates = mutableMapOf<String, MutableMap<String, Pair<Boolean, Boolean>>>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_flat_details)

        val towerNameTextView: TextView = findViewById(R.id.towerNameTextView)
        progressBar = findViewById(R.id.progressBar)
        flatDetailsContainer = findViewById(R.id.flatDetailsContainer)
        updateButton = findViewById(R.id.updateButton)

        towerName = intent.getStringExtra("TOWER_NAME") ?: "Unknown Tower"
        towerNameTextView.text = towerName

        firestore = FirebaseFirestore.getInstance()

        fetchAndDisplayFlatDetails()

        updateButton.setOnClickListener {
            updateFlatDetails()
        }
    }

    private fun fetchAndDisplayFlatDetails() {
        progressBar.visibility = View.VISIBLE
        firestore.collection("towers").document(towerName).collection("floors")
            .get()
            .addOnSuccessListener { floorDocuments ->
                flatDetailsContainer.removeAllViews()
                val sortedFloors = floorDocuments.documents.sortedBy { it.id }
                for (floorDoc in sortedFloors) {
                    val floorNumber = floorDoc.id
                    Log.d("AdminFlatDetailsActivity", "Fetching flats for floor: $floorNumber")
                    val floorUpdatesMap = mutableMapOf<String, Pair<Boolean, Boolean>>()
                    floorUpdates[floorNumber] = floorUpdatesMap

                    firestore.collection("towers").document(towerName)
                        .collection("floors").document(floorNumber).collection("flats")
                        .get()
                        .addOnSuccessListener { flatDocuments ->
                            displayFlats(flatDocuments, floorNumber)
                        }
                        .addOnFailureListener {
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

    private fun displayFlats(flatDocuments: QuerySnapshot, floorNumber: String) {
        val floorContainer = LinearLayout(this)
        floorContainer.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        floorContainer.orientation = LinearLayout.VERTICAL
        floorContainer.setPadding(0, 16, 0, 16)

        val floorTitle = TextView(this)
        floorTitle.text = "$floorNumber"
        floorTitle.textSize = 20f
        floorTitle.setPadding(8, 8, 8, 8)
        floorContainer.addView(floorTitle)

        val floorUpdatesMap = floorUpdates[floorNumber]!!

        for (flatDoc in flatDocuments.documents) {
            val flatNumber = flatDoc.id
            val isOccupiedA = flatDoc.getBoolean("A") ?: false
            val isOccupiedB = flatDoc.getBoolean("B") ?: false
            Log.d("AdminFlatDetailsActivity", "Flat $flatNumber - A: $isOccupiedA, B: $isOccupiedB")

            floorUpdatesMap[flatNumber] = Pair(isOccupiedA, isOccupiedB)

            val flatContainer = LinearLayout(this)
            flatContainer.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            flatContainer.orientation = LinearLayout.HORIZONTAL
            flatContainer.gravity = Gravity.CENTER_VERTICAL
            flatContainer.setPadding(16, 8, 16, 8)

            val flatTextView = TextView(this)
            val params = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            flatTextView.layoutParams = params
            flatTextView.text = flatNumber
            flatTextView.textSize = 18f
            flatTextView.gravity = Gravity.CENTER
            flatTextView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    if (isOccupiedA && isOccupiedB) android.R.color.holo_green_light
                    else android.R.color.holo_red_light
                )
            )
            flatTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            flatTextView.setPadding(16, 8, 16, 8)
            flatContainer.addView(flatTextView)

            // Layout for Room A
            val roomALayout = LinearLayout(this)
            roomALayout.orientation = LinearLayout.VERTICAL
            roomALayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

            val roomALabel = TextView(this)
//            roomALabel.text = ""
            roomALabel.gravity = Gravity.CENTER
            roomALayout.addView(roomALabel)

            val occupiedASpinner = Spinner(this)
            val adapterA = ArrayAdapter.createFromResource(
                this,
                R.array.occupied_options,
                android.R.layout.simple_spinner_item
            )
            adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            occupiedASpinner.adapter = adapterA
            occupiedASpinner.setSelection(if (isOccupiedA) 0 else 1)
            roomALayout.addView(occupiedASpinner)

            flatContainer.addView(roomALayout)

            // Layout for Room B
            val roomBLayout = LinearLayout(this)
            roomBLayout.orientation = LinearLayout.VERTICAL
            roomBLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

            val roomBLabel = TextView(this)
//            roomBLabel.text = ""
            roomBLabel.gravity = Gravity.CENTER
            roomBLayout.addView(roomBLabel)

            val occupiedBSpinner = Spinner(this)
            val adapterB = ArrayAdapter.createFromResource(
                this,
                R.array.occupied_options,
                android.R.layout.simple_spinner_item
            )
            adapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            occupiedBSpinner.adapter = adapterB
            occupiedBSpinner.setSelection(if (isOccupiedB) 0 else 1)
            roomBLayout.addView(occupiedBSpinner)

            flatContainer.addView(roomBLayout)

            occupiedASpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val newStatusA = position == 0
                    floorUpdatesMap[flatNumber] = Pair(newStatusA, floorUpdatesMap[flatNumber]?.second ?: false)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            occupiedBSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val newStatusB = position == 0
                    floorUpdatesMap[flatNumber] = Pair(floorUpdatesMap[flatNumber]?.first ?: false, newStatusB)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            floorContainer.addView(flatContainer)
        }
        flatDetailsContainer.addView(floorContainer)
        progressBar.visibility = View.GONE
    }

    private fun updateFlatDetails() {
        progressBar.visibility = View.VISIBLE
        val updateTasks = mutableListOf<com.google.android.gms.tasks.Task<Void>>()

        for ((floorNumber, flats) in floorUpdates) {
            for ((flatNumber, statuses) in flats) {
                val (isOccupiedA, isOccupiedB) = statuses
                val flatRef = firestore.collection("towers").document(towerName)
                    .collection("floors").document(floorNumber)
                    .collection("flats").document(flatNumber)
                val updateTask = flatRef.update("A", isOccupiedA, "B", isOccupiedB)
                updateTasks.add(updateTask)
            }
        }

        Tasks.whenAllComplete(updateTasks)
            .addOnCompleteListener {
                progressBar.visibility = View.GONE
                if (it.isSuccessful) {
                    Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update some flats", Toast.LENGTH_SHORT).show()
                }
            }
    }
}