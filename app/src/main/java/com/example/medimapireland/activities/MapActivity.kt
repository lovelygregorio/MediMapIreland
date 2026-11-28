package com.example.medimapireland.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medimapireland.R
import com.example.medimapireland.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

// Screen for selecting a healthcare facility location
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    // View Binding connects Kotlin to the XML layout
    private lateinit var binding: ActivityMapBinding

    // Reference to Google Maps
    private lateinit var map: GoogleMap

    // Marker representing the selected facility location
    private var marker: Marker? = null

    // Default location is Dublin, Ireland
    private var latitude = 53.3498
    private var longitude = -6.2603
    private var zoom = 12f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the map screen XML layout
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve existing coordinates if a location was previously selected
        latitude = intent.getDoubleExtra("latitude", 53.3498)
        longitude = intent.getDoubleExtra("longitude", -6.2603)
        zoom = intent.getFloatExtra("zoom", 12f)

        // Disable confirmation until Google Maps is ready
        binding.btnConfirmLocation.isEnabled = false

        // Locate and initialise the Google Maps fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        // Return the selected location when the user confirms
        binding.btnConfirmLocation.setOnClickListener {
            confirmLocation()
        }
    }

    // Called when Google Maps has finished loading
    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap

        // Create the initial healthcare facility location
        val location = LatLng(latitude, longitude)

        // Add a draggable marker to the map
        marker = map.addMarker(
            MarkerOptions()
                .position(location)
                .title("Selected Healthcare Facility")
                .draggable(true)
        )

        // Move the camera to the selected location
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(location, zoom)
        )

        // Enable map zoom controls
        map.uiSettings.isZoomControlsEnabled = true

        // Allow the user to select a location by tapping the map
        map.setOnMapClickListener { selectedLocation ->

            // Move the marker to the selected position
            marker?.position = selectedLocation

            // Update the selected coordinates
            latitude = selectedLocation.latitude
            longitude = selectedLocation.longitude
        }

        // Listen for changes when the marker is dragged
        map.setOnMarkerDragListener(
            object : GoogleMap.OnMarkerDragListener {

                override fun onMarkerDragStart(marker: Marker) {
                    // No action required when dragging begins
                }

                override fun onMarkerDrag(marker: Marker) {
                    // No action required while dragging
                }

                override fun onMarkerDragEnd(marker: Marker) {

                    // Update coordinates when dragging finishes
                    latitude = marker.position.latitude
                    longitude = marker.position.longitude
                }
            }
        )

        // Enable confirmation once the map is ready
        binding.btnConfirmLocation.isEnabled = true
    }

    // Return the selected coordinates to the healthcare facility form
    private fun confirmLocation() {

        // Retrieve the current map zoom level
        zoom = map.cameraPosition.zoom

        // Create an Intent containing the selected location
        val resultIntent = Intent()

        resultIntent.putExtra("latitude", latitude)
        resultIntent.putExtra("longitude", longitude)
        resultIntent.putExtra("zoom", zoom)

        // Return the selected location to the previous activity
        setResult(RESULT_OK, resultIntent)

        // Close the map screen
        finish()
    }
}