
package com.example.medimapireland.main

import android.app.Application
import com.example.medimapireland.models.HealthcareFacilityMemStore
import com.example.medimapireland.models.HealthcareFacilityStore

// Main application class for MediMap Ireland
class MainApp : Application() {

    // Shared storage for healthcare facilities
    lateinit var facilities: HealthcareFacilityStore

    override fun onCreate() {
        super.onCreate()

        // Initialise temporary memory storage
        facilities = HealthcareFacilityMemStore()
    }
}
