package com.example.medimapireland.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medimapireland.databinding.ItemHealthcareFacilityBinding
import com.example.medimapireland.models.HealthcareFacilityModel

// Adapter connects our healthcare facility data to the RecyclerView
class HealthcareFacilityAdapter(
    private val facilities: List<HealthcareFacilityModel>
) : RecyclerView.Adapter<HealthcareFacilityAdapter.FacilityViewHolder>() {

    // ViewHolder holds the views for one healthcare facility item
    class FacilityViewHolder(
        val binding: ItemHealthcareFacilityBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // Creates the layout for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacilityViewHolder {

        // Convert item_healthcare_facility.xml into a View Binding object
        val binding = ItemHealthcareFacilityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FacilityViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FacilityViewHolder,
        position: Int
    ) {
        // Get the facility at the current list position
        val facility = facilities[position]

        // Display its details using the XML TextViews
        holder.binding.tvFacilityName.text = facility.name
        holder.binding.tvFacilityCounty.text = "County: ${facility.county}"
        holder.binding.tvFacilityType.text = "Type: ${facility.facilityType}"
        holder.binding.tvFacilityOwnership.text = "Ownership: ${facility.ownership}"
    }

    // Tell RecyclerView how many facilities to display
    override fun getItemCount(): Int = facilities.size
}