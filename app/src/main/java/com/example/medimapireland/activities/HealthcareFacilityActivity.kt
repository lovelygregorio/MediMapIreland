
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the healthcare facility XML layout
        binding = ActivityHealthcareFacilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the application and its facility storage
        app = application as MainApp

        // Listen for the Save button being clicked
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

                // Create a healthcare facility object
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

                // Display confirmation
                Toast.makeText(
                    this,
                    "Healthcare facility saved!",
                    Toast.LENGTH_SHORT
                ).show()

                // Close this screen and return to the previous one
                finish()
            }
        }
    }
}
