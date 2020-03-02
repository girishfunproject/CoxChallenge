package com.mvvm.girishdemo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseFragment
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.utils.Utils
import com.mvvm.girishdemo.utils.gone
import com.mvvm.girishdemo.utils.visible
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

/**
 * Created by Girish Sigicherla on 3/1/2020.
 */
class HomeFragment @Inject constructor(
    private val utils: Utils
) : BaseFragment() {

    private lateinit var coxViewModel: CoxViewModel

    override fun getLayoutResId(): Int = R.layout.home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coxViewModel = (activity as MainActivity).getViewModel()
        setupButtonOnClick()
        getVehicleList()
    }

    private fun getVehicleList() {
        if (!utils.isDataBaseCreated()) {
            progress_bar.visible()
            //make necessary API calls and store in DB for the first time
            coxViewModel.getVehicleList()
            //call to get all vehicles info and then we enable the Get Dealers button
            coxViewModel.getVehicleListResult()
                .observe(viewLifecycleOwner, Observer<List<Vehicle>> {
                    Log.d("$TAG : From API", it.toString())
                    Toast.makeText(
                        context,
                        "All vehicles retrieved from server",
                        Toast.LENGTH_SHORT
                    ).show()
                    coxViewModel.getVehicleListFromDB()
                })
        } else {
            progress_bar.visible()
            coxViewModel.getVehicleListFromDB()
        }

        coxViewModel.getVehicleListDBResult().observe(viewLifecycleOwner, Observer<List<Vehicle>> {
            Log.d("$TAG : From DATABASE", it.toString())
            progress_bar.gone()
            get_dealers.visible()
        })

        coxViewModel.getVehicleListError().observe(viewLifecycleOwner, Observer<String> {
            progress_bar.gone()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupButtonOnClick() {
        get_dealers.setOnClickListener {
            //start dealer fragment
            loadFragment(DealersFragment(utils))
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, fragment)
            ?.addToBackStack(null)?.commit()
    }

    private companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}