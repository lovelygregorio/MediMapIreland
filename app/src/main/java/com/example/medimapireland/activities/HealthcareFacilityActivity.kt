
package com.example.medimapireland.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medimapireland.databinding.ActivityHealthcareFacilityBinding
import com.example.medimapireland.main.MainApp
import com.example.medimapireland.models.HealthcareFacilityModel


// Screen for adding a new healthcare facility
class HealthcareFacilityActivity : AppCompatActivity() {

    // View Binding connects Kotlin to the XML layout
    private lateinit var binding: ActivityHealthcareFacilityBinding

    // Access the shared application storage
    private lateinit var app: MainApp

    // Holds the existing facility when editing
    // Null means we are adding a new facility
    private var editingFacility: HealthcareFacilityModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the healthcare facility XML layout
        binding = ActivityHealthcareFacilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the application and its facility storage
        app = application as MainApp

        // Check whether a facility ID was passed for editing
        if (intent.hasExtra("facility_id")) {

            val facilityId = intent.getLongExtra("facility_id", -1L)

            // Find the existing facility using its ID
            editingFacility = app.facilities.findAll().find {
                it.id == facilityId
            }

            // Display the existing facility details
            val facility = editingFacility

            if (facility != null) {

                binding.facilityName.setText(facility.name)
                binding.facilityDescription.setText(facility.description)
                binding.facilityCounty.setText(facility.county)
                binding.facilityType.setText(facility.facilityType)
                binding.facilityOwnership.setText(facility.ownership)
                binding.facilityServices.setText(facility.services)

                // Change the button text when editing
                binding.btnSaveFacility.text = "Update Facility"

            } else {

                // Close the screen if the facility cannot be found
                Toast.makeText(
                    this,
                    "Healthcare facility not found",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
                return
            }
        }

        // Listen for the Save or Update button being clicked
        binding.btnSaveFacility.setOnClickListener {

            // Read the values entered by the user
            val name = binding.facilityName.text.toString().trim()
            val description = binding.facilityDescription.text.toString().trim()
            val county = binding.facilityCounty.text.toString().trim()
            val facilityType = binding.facilityType.text.toString().trim()
            val ownership = binding.facilityOwnership.text.toString().trim()
            val services = binding.facilityServices.text.toString().trim()

            // Validate the required fields
            if (name.isBlank() || county.isBlank() ||
                facilityType.isBlank() || ownership.isBlank()) {

                Toast.makeText(
                    this,
                    "Please complete all required fields",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val existingFacility = editingFacility

                if (existingFacility != null) {

                    // Update the existing facility while preserving
                    // its ID, latitude, longitude and zoom
                    val updatedFacility = existingFacility.copy(
                        name = name,
                        description = description,
                        county = county,
                        facilityType = facilityType,
                        ownership = ownership,
                        services = services
                    )

                    // Update the facility in shared storage
                    app.facilities.update(updatedFacility)

                    // Display update confirmation
                    Toast.makeText(
                        this,
                        "Healthcare facility updated!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    // Create a new healthcare facility object
                    val facility = HealthcareFacilityModel(
                        name = name,
                        description = description,
                        county = county,
                        facilityType = facilityType,
                        ownership = ownership,
                        services = services
                    )

                    // Save the facility to temporary memory storage
                    app.facilities.create(facility)

                    // Display save confirmation
                    Toast.makeText(
                        this,
                        "Healthcare facility saved!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Close this screen and return to the previous one
                finish()
            }
        }
    }
}