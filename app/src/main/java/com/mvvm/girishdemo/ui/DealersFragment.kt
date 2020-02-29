package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseFragment
import com.mvvm.girishdemo.model.Vehicle


/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

class DealersFragment : BaseFragment() {

    private var sharedPreference: SharedPreferences? = null
    private var coxViewModel: CoxViewModel? = null
    private var vehicleIdList: List<String>? = null
    override fun getLayoutResId(): Int = R.layout.dealers_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreference = context?.getSharedPreferences("cox_prefs", Context.MODE_PRIVATE)
        coxViewModel = (activity as MainActivity).getViewModel()
        vehicleIdList = (activity as MainActivity).getVehicleIdList()
        //setuprecyclerView()
        //setUpListeners()
        //observeViewModelData()


        fetchVehicleData()
    }

    //first time make api call to get vehicle infos
    private fun fetchVehicleData() {

        coxViewModel?.getVehicleInfoListFromApi(vehicleIdList)

        coxViewModel?.getVehicleInfoListResult()?.observe(viewLifecycleOwner,
            Observer<List<Vehicle>> {
                Log.d("Vehicle Info:", "$it")
            }
        )
    }


}