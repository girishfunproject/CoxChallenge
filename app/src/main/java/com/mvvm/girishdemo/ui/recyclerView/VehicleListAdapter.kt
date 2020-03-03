package com.mvvm.girishdemo.ui.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.ui.recyclerView.DealerListAdapter.DealerViewHolder

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */

/**
 * A standard adapter used to display a list of vehicles. This adapter takes the vehicles provided by the view model and serves those to @see[CoxRecyclerView]
 */
class VehicleListAdapter : RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder>() {

    /**
     * List of vehicles that will be populated in the recycler view
     */
    var vehicles: List<Vehicle> = emptyList()

    /**
     * Called when RecyclerView needs a new @see[VehicleViewHolder] to represent an item.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicleViewHolder {
        val vehicleView =
            LayoutInflater.from(parent.context).inflate(R.layout.vehicle_item_view, parent, false)
        return VehicleViewHolder(vehicleView)
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = vehicles.size

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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