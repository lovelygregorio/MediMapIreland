package com.example.medimapireland.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medimapireland.adapters.HealthcareFacilityAdapter
import com.example.medimapireland.adapters.HealthcareFacilityListener
import com.example.medimapireland.databinding.ActivityHealthcareFacilityListBinding
import com.example.medimapireland.main.MainApp
import com.example.medimapireland.models.HealthcareFacilityModel
import android.content.Intent

// Screen for displaying a list of healthcare facilities
class HealthcareFacilityListActivity : AppCompatActivity(),
    HealthcareFacilityListener {

    // View Binding connects Kotlin to the XML layout
    private lateinit var binding: ActivityHealthcareFacilityListBinding

    // Access the shared application storage
    private lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the healthcare facility list XML layout
        binding = ActivityHealthcareFacilityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the application and its facility storage
        app = application as MainApp

        // Arrange healthcare facilities in a vertical scrolling list
        binding.recyclerViewFacilities.layoutManager =
            LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        // Refresh the list whenever the activity resumes
        loadFacilities()
    }

    // Retrieve the latest healthcare facilities
    private fun loadFacilities() {

        val facilities = app.facilities.findAll()

        // Connect the saved facilities to the RecyclerView
        binding.recyclerViewFacilities.adapter =
            HealthcareFacilityAdapter(facilities, this)
    }

    // Called when the user selects a healthcare facility
    override fun onFacilityClick(facility: HealthcareFacilityModel) {

        // Available actions for the selected facility
        val options = arrayOf("Edit Facility", "Delete Facility")

        // Display the available actions
        AlertDialog.Builder(this)
            .setTitle(facility.name)
            .setItems(options) { _, which ->

                when (which) {
                    0 -> editFacility(facility)
                    1 -> deleteFacility(facility)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Open the selected healthcare facility for editing
    private fun editFacility(facility: HealthcareFacilityModel) {

        // Create an Intent to open the healthcare facility form
        val intent = Intent(this, HealthcareFacilityActivity::class.java)

        // Pass the ID of the selected facility to the activity
        intent.putExtra("facility_id", facility.id)

        // Open the healthcare facility activity
        startActivity(intent)
    }

    // Confirm and delete the selected healthcare facility
    private fun deleteFacility(facility: HealthcareFacilityModel) {

        // Ask the user to confirm before deleting
        AlertDialog.Builder(this)
            .setTitle("Delete Healthcare Facility")
            .setMessage("Are you sure you want to delete ${facility.name}?")

            // Delete the facility if the user confirms
            .setPositiveButton("Delete") { _, _ ->

                // Remove the facility from the shared storage
                app.facilities.delete(facility)

                // Refresh the RecyclerView
                loadFacilities()
            }

            // Cancel without deleting the facility
            .setNegativeButton("Cancel", null)
            .show()
    }
}