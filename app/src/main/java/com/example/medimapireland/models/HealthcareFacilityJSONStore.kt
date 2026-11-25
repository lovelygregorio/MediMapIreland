package com.example.medimapireland.models

import android.content.Context
import com.example.medimapireland.helpers.exists
import com.example.medimapireland.helpers.read
import com.example.medimapireland.helpers.write
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

// Name of the JSON file
private const val JSON_FILE = "healthcare_facilities.json"

// Gson converts Kotlin objects to and from JSON
private val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

// Define the type of objects stored in the JSON file
private val listType =
    object : TypeToken<ArrayList<HealthcareFacilityModel>>() {}.type

// Persistent storage for healthcare facilities
class HealthcareFacilityJSONStore(
    private val context: Context
) : HealthcareFacilityStore {

    // List of healthcare facilities
    private var facilities = ArrayList<HealthcareFacilityModel>()

    // Generates a unique ID for each new facility
    private var nextId = 1L

    // Load saved facilities when the application starts
    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }

        // Continue from the highest saved ID
        nextId = (facilities.maxOfOrNull { it.id } ?: 0L) + 1L
    }

    // READ: Return all healthcare facilities
    override fun findAll(): List<HealthcareFacilityModel> {
        return facilities.toList()
    }

    // CREATE: Add a new healthcare facility
    override fun create(facility: HealthcareFacilityModel) {

        // Assign a unique ID
        facility.id = nextId++

        // Add the facility to the list
        facilities.add(facility)

        // Save the updated list to JSON
        serialize()
    }

    // UPDATE: Edit an existing healthcare facility
    override fun update(facility: HealthcareFacilityModel) {

        // Find the facility using its unique ID
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

            // Save the updated details to JSON
            serialize()
        }
    }

    // DELETE: Remove a healthcare facility
    override fun delete(facility: HealthcareFacilityModel) {

        // Remove the record with the matching ID
        facilities.removeAll { it.id == facility.id }

        // Save the updated list to JSON
        serialize()
    }

    // Convert Kotlin objects into JSON and save to a file
    private fun serialize() {

        val jsonString = gson.toJson(facilities, listType)

        write(context, JSON_FILE, jsonString)
    }

    // Read saved JSON and convert it back into Kotlin objects
    private fun deserialize() {

        val jsonString = read(context, JSON_FILE)

        facilities = gson.fromJson<ArrayList<HealthcareFacilityModel>>(
            jsonString,
            listType
        ) ?: ArrayList()
    }
}