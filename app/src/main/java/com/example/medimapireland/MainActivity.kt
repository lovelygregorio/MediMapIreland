
package com.example.medimapireland

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medimapireland.activities.HealthcareFacilityActivity
import com.example.medimapireland.activities.HealthcareFacilityListActivity
import com.example.medimapireland.databinding.ActivityMainBinding

// Main screen of MediMap Ireland
class MainActivity : AppCompatActivity() {

    // Connect Kotlin to the activity_main.xml layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open the Add Healthcare Facility screen
        binding.btnAddFacility.setOnClickListener {

            // Intent tells Android which Activity to open
            val intent = Intent(
                this,
                HealthcareFacilityActivity::class.java
            )
            startActivity(intent)
        }

        // Open the Healthcare Facility List screen
        binding.btnViewFacilities.setOnClickListener {

            // Create an Intent to navigate to the facility list
            val intent = Intent(
                this,
                HealthcareFacilityListActivity::class.java
            )
            // Launch the screen displaying saved facilities
            startActivity(intent)
        }
    }
}
