package com.example.medimapireland.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medimapireland.adapters.HealthcareFacilityAdapter
import com.example.medimapireland.databinding.ActivityHealthcareFacilityListBinding
import com.example.medimapireland.main.MainApp

// Screen for displaying a list of healthcare facilities
class HealthcareFacilityListActivity : AppCompatActivity() {


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

        // Retrieve the latest facilities whenever this screen opens
        val facilities = app.facilities.findAll()

        // Connect the saved facilities to the RecyclerView
        binding.recyclerViewFacilities.adapter =
            HealthcareFacilityAdapter(facilities)
    }
}

