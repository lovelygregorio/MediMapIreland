package com.example.medimapireland.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medimapireland.R
import com.example.medimapireland.adapters.HealthcareFacilityAdapter
import com.example.medimapireland.adapters.HealthcareFacilityListener
import com.example.medimapireland.databinding.ActivityHealthcareFacilityListBinding
import com.example.medimapireland.main.MainApp
import com.example.medimapireland.models.HealthcareFacilityModel

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


        // Initialise the filter dropdown menus
        setupFilterMenus()

        // Enable live searching
        setupSearchListener()

        // Reset the search and filters when the button is clicked
        binding.btnClearFilters.setOnClickListener {
            clearFilters()
        }
    }


    override fun onResume() {
        super.onResume()

        // Refresh the list while preserving the selected filters
        applyFilters()
    }

    // Configure the county, facility type and ownership filters
    private fun setupFilterMenus() {

        // Load the counties and include an option to display all counties
        val counties = arrayOf("All Counties") +
                resources.getStringArray(R.array.irish_counties)

        val countyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            counties
        )

        binding.filterCounty.setAdapter(countyAdapter)
        binding.filterCounty.setText("All Counties", false)

        // Load the facility types and include an option to display all types
        val facilityTypes = arrayOf("All Types") +
                resources.getStringArray(R.array.facility_types)

        val typeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            facilityTypes
        )

        binding.filterFacilityType.setAdapter(typeAdapter)
        binding.filterFacilityType.setText("All Types", false)

        // Load the ownership categories and include an option to display all
        val ownershipTypes = arrayOf("All Ownership Types") +
                resources.getStringArray(R.array.ownership_types)

        val ownershipAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            ownershipTypes
        )

        binding.filterOwnership.setAdapter(ownershipAdapter)
        binding.filterOwnership.setText("All Ownership Types", false)

        // Apply filters whenever the selected county changes
        binding.filterCounty.setOnItemClickListener { _, _, _, _ ->
            applyFilters()
        }

        // Apply filters whenever the selected facility type changes
        binding.filterFacilityType.setOnItemClickListener { _, _, _, _ ->
            applyFilters()
        }

        // Apply filters whenever the selected ownership changes
        binding.filterOwnership.setOnItemClickListener { _, _, _, _ ->
            applyFilters()
        }
    }

    // Listen for changes in the search field
    private fun setupSearchListener() {

        // Update the results whenever the user enters or removes text
        binding.searchFacility.doAfterTextChanged {
            applyFilters()
        }
    }

    // Search and filter healthcare facilities
    private fun applyFilters() {

        // Retrieve all healthcare facilities from shared JSON storage
        val facilities = app.facilities.findAll()

        // Read the search keyword and selected filter values
        val searchQuery = binding.searchFacility.text.toString().trim()
        val selectedCounty = binding.filterCounty.text.toString()
        val selectedType = binding.filterFacilityType.text.toString()
        val selectedOwnership = binding.filterOwnership.text.toString()

        // Filter facilities according to the user's selections
        val filteredFacilities = facilities.filter { facility ->

            // Search across facility name, description and services
            // Ignore uppercase and lowercase differences
            val matchesSearch =
                searchQuery.isBlank() ||
                        facility.name.contains(searchQuery, ignoreCase = true) ||
                        facility.description.contains(searchQuery, ignoreCase = true) ||
                        facility.services.contains(searchQuery, ignoreCase = true)

            // Check whether the facility matches the selected county
            val matchesCounty =
                selectedCounty == "All Counties" ||
                        facility.county.equals(selectedCounty, ignoreCase = true)

            // Check whether the facility matches the selected facility type
            val matchesType =
                selectedType == "All Types" ||
                        facility.facilityType.equals(selectedType, ignoreCase = true)

            // Check whether the facility matches the selected ownership
            val matchesOwnership =
                selectedOwnership == "All Ownership Types" ||
                        facility.ownership.equals(selectedOwnership, ignoreCase = true)

            // Only include facilities that match all active filters
            matchesSearch && matchesCounty &&
                    matchesType && matchesOwnership
        }

        // Display the filtered facilities in the RecyclerView
        binding.recyclerViewFacilities.adapter =
            HealthcareFacilityAdapter(filteredFacilities, this)

        // Display the number of matching facilities
        val count = filteredFacilities.size

        binding.tvResultsCount.text = if (count == 1) {
            "1 facility found"
        } else {
            "$count facilities found"
        }

        // Display an empty-results message when no facilities match
        if (filteredFacilities.isEmpty()) {
            binding.tvEmptyResults.visibility = View.VISIBLE
            binding.recyclerViewFacilities.visibility = View.GONE
        } else {
            binding.tvEmptyResults.visibility = View.GONE
            binding.recyclerViewFacilities.visibility = View.VISIBLE
        }
    }

    // Reset the search field and all selected filters
    private fun clearFilters() {

        // Restore the default filter selections
        binding.filterCounty.setText("All Counties", false)
        binding.filterFacilityType.setText("All Types", false)
        binding.filterOwnership.setText("All Ownership Types", false)

        // Clear the search keyword
        binding.searchFacility.text?.clear()

        // Refresh the list to display all healthcare facilities
        applyFilters()
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

                // Refresh the list while preserving active filters
                applyFilters()
            }
            // Cancel without deleting the facility
            .setNegativeButton("Cancel", null)
            .show()
    }
}