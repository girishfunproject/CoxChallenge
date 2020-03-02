package com.mvvm.girishdemo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseFragment
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.ui.recyclerView.CoxRecyclerView
import com.mvvm.girishdemo.ui.recyclerView.ItemDecorator
import com.mvvm.girishdemo.ui.recyclerView.VehicleListAdapter

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */
class VehiclesFragment(var dealerId: Int) : BaseFragment() {
    override fun getLayoutResId(): Int = R.layout.vehicles_fragment
    private lateinit var coxViewModel: CoxViewModel
    private var vehiclesRecyclerView: CoxRecyclerView? = null
    private var vehicleAdapter = VehicleListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coxViewModel = (activity as MainActivity).getViewModel()
        setUpRecyclerView()
        getVehicleList(dealerId)
        (activity as MainActivity).setTitle(R.string.vehicles_title)
    }

    private fun setUpRecyclerView() {
        vehiclesRecyclerView = view?.findViewById(R.id.vehicleList)
        vehiclesRecyclerView?.apply {
            adapter = vehicleAdapter
            isHorizontal = true
        }
    }

    private fun getVehicleList(dealerId: Int) {
        coxViewModel.getVehicleListForDealerFromDB(dealerId)

        coxViewModel.getVehicleListForDealerDBResult()
            .observe(viewLifecycleOwner, Observer<List<Vehicle>> {
                Log.d("VehiclesFragment: for dealer From DATABASE", it.toString())
                Toast.makeText(context, "All Vehicles fetched from Database", Toast.LENGTH_SHORT).show()

                vehicleAdapter.apply {
                    vehicles = it
                    notifyDataSetChanged()
                }
            })
    }
}