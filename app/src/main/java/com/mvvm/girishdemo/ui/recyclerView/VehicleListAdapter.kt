package com.mvvm.girishdemo.ui.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.model.Vehicle

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */
class VehicleListAdapter : RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder>() {

    var vehicles: List<Vehicle> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicleViewHolder {
        val vehicleView =
            LayoutInflater.from(parent.context).inflate(R.layout.vehicle_item_view, parent, false)
        return VehicleViewHolder(vehicleView)
    }

    override fun getItemCount(): Int = vehicles.size

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) =
        holder.bind(vehicles[position])

    inner class VehicleViewHolder(vehicleView: View) : RecyclerView.ViewHolder(vehicleView) {
        private val year: TextView = vehicleView.findViewById(R.id.year)
        private val make: TextView = vehicleView.findViewById(R.id.make)
        private val model: TextView = vehicleView.findViewById(R.id.model)
        private val vehicleId: TextView = vehicleView.findViewById(R.id.vehicle_id)
        private val dealerId: TextView = vehicleView.findViewById(R.id.dealer_id)

        fun bind(vehicle: Vehicle) {
            year.text = vehicle.year.toString()
            make.text = vehicle.make
            model.text = vehicle.model
            vehicleId.text = vehicle.vehicleId.toString()
            dealerId.text = vehicle.dealerId.toString()
        }

    }
}