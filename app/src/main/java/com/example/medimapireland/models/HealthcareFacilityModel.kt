
package com.example.medimapireland.models

// HealthcareFacilityModel: Data class representing a healthcare facility
data class HealthcareFacilityModel(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var county: String = "",
    var facilityType: String = "",
    var ownership: String = "",
    var services: String = "",
    var lat: Double = 53.3498,
    var lng: Double = -6.2603,
    var zoom: Float = 12f
)
