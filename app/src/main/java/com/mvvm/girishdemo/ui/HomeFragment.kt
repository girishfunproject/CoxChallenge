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

/**
 * This fragment is attached to the @see[MainActivity] and will present a button to launch @see[DealersFragment]
 * In this class we observe live data objects to be able to know when all the vehicles are retrieved from the server and
 * when all the vehicles are stored to the database.
 * Once we get these events back from the view model we hide the progress bar and show the Get Dealers button on the UI to launch DealersFragment.
 * The live data objects for API calls are being used just to show toast messages or print logs
 * NOTE: All the data that is populated on the UI is fetched from the DATABASE only.
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

    /**
     * Before making API calls to the server we first check if all required tables are created in the database. This flag is set once we finish
     * retrieving all dealers in the Dealers Fragment and storing them in the database.
     * If the database is already created we directly fetch data from the database.
     *
     * NOTE: No data is shown in this fragment, We just observe live data objects here to determine when to show progress bar , getDealers button and proper toast messages
     */
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

    /**
     * start dealer fragment
     */
    private fun setupButtonOnClick() {
        get_dealers.setOnClickListener {
            loadFragment(DealersFragment(utils))
        }
    }

    /**
     * create a FragmentTransaction to begin the transaction and replace the Fragment
     */
    private fun loadFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, fragment)
            ?.addToBackStack(null)?.commit()
    }

    private companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}