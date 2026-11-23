package com.example.medimapireland.models


// Temporary storage for healthcare facilities
// Implements the HealthcareFacilityStore interface
class HealthcareFacilityMemStore : HealthcareFacilityStore {

    private val facilities = ArrayList<HealthcareFacilityModel>()

    // Generates a unique ID for each new facility
    private var nextId = 1L


    // READ: Return all healthcare facilities
    override fun findAll(): List<HealthcareFacilityModel> {
        return facilities.toList()
    }

    // CREATE: Add a new healthcare facility
    override fun create(facility: HealthcareFacilityModel) {

        // Assign a unique ID and increment the counter
        facility.id = nextId++

        // Add the facility to the list
        facilities.add(facility)
    }

    // UPDATE: Edit an existing healthcare facility
    override fun update(facility: HealthcareFacilityModel) {

        // Search for the facility using its unique ID
        val found = facilities.find { it.id == facility.id }


        // Update the details if the facility exists
        if (found != null) {
            found.name = facility.name
            found.description = facility.description
            found.county = facility.county
            found.facilityType = facility.facilityType
            found.ownership = facility.ownership
            found.services = facility.services
            found.lat = facility.lat
            found.lng = facility.lng
            found.zoom = facility.zoom
        }
    }

    // DELETE: Remove a healthcare facility
    override fun delete(facility: HealthcareFacilityModel) {

        // Remove the record with the matching ID
        facilities.removeAll { it.id == facility.id }
    }
}