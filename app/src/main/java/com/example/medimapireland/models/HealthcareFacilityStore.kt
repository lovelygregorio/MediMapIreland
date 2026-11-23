package com.example.medimapireland.models

// HealthcareFacilityStore: Interface for healthcare facility storage
interface HealthcareFacilityStore {
    fun findAll(): List<HealthcareFacilityModel>
    fun create(facility: HealthcareFacilityModel)
    fun update(facility: HealthcareFacilityModel)
    fun delete(facility: HealthcareFacilityModel)
}