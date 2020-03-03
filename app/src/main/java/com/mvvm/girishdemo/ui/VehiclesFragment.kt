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

/**
 * A fragment class used to populate list of vehicles for a given dealer id.
 * The vehicle list is populated on a custom recycler view @see[CoxRecyclerView] in horizontal fashion.
 * We observe the live data object that notifies the view when vehicle list is fetched from the databse.
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

    /**
     * To set up a recyler view to show list of vehicles of type @see[CoxRecyclerView]
     * The recycler view will populate the list of vehicles horizontally
     */
    private fun setUpRecyclerView() {
        vehiclesRecyclerView = view?.findViewById(R.id.vehicleList)
        vehiclesRecyclerView?.apply {
            adapter = vehicleAdapter
            isHorizontal = true
        }
    }

    /**
     * The vehicle list is fetched from the database using live data
     */
    private fun getVehicleList(dealerId: Int) {
        coxViewModel.apply {
            getVehicleListForDealerFromDB(dealerId)

            getVehicleListForDealerDBResult()
                .observe(viewLifecycleOwner, Observer<List<Vehicle>> {
                    Log.d("$TAG : for dealer From DATABASE", it.toString())
                    Toast.makeText(
                        context,
                        getString(R.string.vehicles_fetched_msg),
                        Toast.LENGTH_SHORT
                    ).show()

                    vehicleAdapter.apply {
                        vehicles = it
                        notifyDataSetChanged()
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setTitle(R.string.dealers_title)
    }

    private companion object {
        private val TAG = VehiclesFragment::class.java.simpleName
    }
}