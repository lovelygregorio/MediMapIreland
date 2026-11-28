package com.example.medimapireland.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.medimapireland.R
import com.example.medimapireland.databinding.ActivityHealthcareFacilityBinding
import com.example.medimapireland.main.MainApp
import com.example.medimapireland.models.HealthcareFacilityModel

// Screen for adding or editing a healthcare facility
class HealthcareFacilityActivity : AppCompatActivity() {

    // View Binding connects Kotlin to the XML layout
    private lateinit var binding: ActivityHealthcareFacilityBinding

    // Access the shared application storage
    private lateinit var app: MainApp

    // Holds the existing facility when editing
    // Null means we are adding a new facility
    private var editingFacility: HealthcareFacilityModel? = null

    // Default map coordinates for Dublin, Ireland
    private var latitude = 53.3498
    private var longitude = -6.2603
    private var zoom = 12f

    // Tracks whether the user has selected a location
    private var locationSelected = false

    // Receive the selected location returned by MapActivity
    private val mapLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data

            if (data != null) {
                // Retrieve the selected coordinates and zoom level
                latitude = data.getDoubleExtra("latitude", latitude)
                longitude = data.getDoubleExtra("longitude", longitude)
                zoom = data.getFloatExtra("zoom", zoom)

                // Mark the location as selected
                locationSelected = true

                // Display the selected location on the form
                displaySelectedLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the healthcare facility XML layout
        binding = ActivityHealthcareFacilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the application and its facility storage
        app = application as MainApp

        // Initialise the dropdown menus
        setupDropdownMenus()

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

                // Restore saved selections without filtering dropdown options
                binding.facilityCounty.setText(facility.county, false)
                binding.facilityType.setText(facility.facilityType, false)
                binding.facilityOwnership.setText(facility.ownership, false)

                binding.facilityServices.setText(facility.services)

                // Retrieve the previously saved map coordinates
                latitude = facility.lat
                longitude = facility.lng
                zoom = facility.zoom

                // Older facilities may still contain the default coordinates
                locationSelected = latitude != 53.3498 ||
                        longitude != -6.2603


                // Change the screen heading when editing
                binding.tvFormHeading.text = "Edit Healthcare Facility"

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


        // Restore the selected location after activity recreation
        if (savedInstanceState != null) {
            latitude = savedInstanceState.getDouble("latitude", latitude)
            longitude = savedInstanceState.getDouble("longitude", longitude)
            zoom = savedInstanceState.getFloat("zoom", zoom)
            locationSelected = savedInstanceState.getBoolean(
                "locationSelected",
                locationSelected
            )
        }

        // Display the current location selection
        displaySelectedLocation()

        // Open Google Maps when the Set Location button is clicked
        binding.btnSetLocation.setOnClickListener {
            val mapIntent = Intent(this, MapActivity::class.java)

            // Pass the current coordinates to the map screen
            mapIntent.putExtra("latitude", latitude)
            mapIntent.putExtra("longitude", longitude)
            mapIntent.putExtra("zoom", zoom)

            // Launch the map and wait for the selected location
            mapLauncher.launch(mapIntent)
        }

        // Listen for the Save or Update button being clicked
        binding.btnSaveFacility.setOnClickListener {

            // Read the values entered or selected by the user
            val name = binding.facilityName.text.toString().trim()
            val description = binding.facilityDescription.text.toString().trim()
            val county = binding.facilityCounty.text.toString().trim()
            val facilityType = binding.facilityType.text.toString().trim()
            val ownership = binding.facilityOwnership.text.toString().trim()
            val services = binding.facilityServices.text.toString().trim()

            // Validate the required fields
            if (name.isBlank() || county.isBlank() ||
                facilityType.isBlank() || ownership.isBlank()
            ) {
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

                    // Update the facility in shared JSON storage
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
                        services = services,
                        lat = latitude,
                        lng = longitude,
                        zoom = zoom
                    )

                    // Save the facility to persistent JSON storage
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

    // Preserve selected coordinates when the activity is recreated
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble("latitude", latitude)
        outState.putDouble("longitude", longitude)
        outState.putFloat("zoom", zoom)
        outState.putBoolean("locationSelected", locationSelected)

        super.onSaveInstanceState(outState)
    }

    // Display the selected map coordinates on the form
    private fun displaySelectedLocation() {
        if (locationSelected) {
            binding.tvSelectedLocation.text =
                "Location selected\nLatitude: %.5f\nLongitude: %.5f"
                    .format(latitude, longitude)

            binding.btnSetLocation.text = "Change Location"
        } else {
            binding.tvSelectedLocation.text = "No location selected"
            binding.btnSetLocation.text = "Set Location"
        }
    }

    // Configure the dropdown menus using string arrays
    private fun setupDropdownMenus() {

        // Load all 32 Irish counties from strings.xml
        val counties = resources.getStringArray(R.array.irish_counties)

        // Create an adapter to display the county options
        val countyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            counties
        )

        // Connect the adapter to the county dropdown
        binding.facilityCounty.setAdapter(countyAdapter)

        // Load the available healthcare facility types
        val facilityTypes = resources.getStringArray(R.array.facility_types)

        // Create an adapter to display the facility type options
        val typeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            facilityTypes
        )

        // Connect the adapter to the facility type dropdown
        binding.facilityType.setAdapter(typeAdapter)

        // Load the available ownership categories
        val ownershipTypes = resources.getStringArray(R.array.ownership_types)

        // Create an adapter to display the ownership options
        val ownershipAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            ownershipTypes
        )

        // Connect the adapter to the ownership dropdown
        binding.facilityOwnership.setAdapter(ownershipAdapter)
    }
}