package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseActivity
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.utils.Utils
import com.mvvm.girishdemo.utils.gone
import com.mvvm.girishdemo.utils.visible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_main

    @Inject
    lateinit var coxViewModelFactory: CoxViewModelFactory
    lateinit var coxViewModel: CoxViewModel
    lateinit var utils: Utils
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        utils = Utils(context = this)
        setupButtonOnClick()
        coxViewModel =
            ViewModelProviders.of(this, coxViewModelFactory).get(CoxViewModel::class.java)
        sharedPreference = getSharedPreferences("cox_prefs", Context.MODE_PRIVATE)
        getVehicleList()
    }

    private fun getVehicleList() {
        if (!utils.isDataBaseCreated()) {
            progressBar.visible()
            //make necessary API calls and store in DB for the first time
            coxViewModel.getVehicleList()
            //call to get all vehicles info and then we enable the Get Dealers button
            coxViewModel.getVehicleListResult().observe(this, Observer<List<Vehicle>> {
                Log.d("MainActivity: From API", it.toString())
                Toast.makeText(this, "All vehicles retrieved from server", Toast.LENGTH_SHORT).show()
                coxViewModel.getVehicleListFromDB()
            })
        } else {
            progressBar.visible()
            coxViewModel.getVehicleListFromDB()
        }

        coxViewModel.getVehicleListDBResult().observe(this, Observer<List<Vehicle>> {
            Log.d("MainActivity: From DATABASE", it.toString())
            progressBar.gone()
            get_dealers.visible()
        })

        coxViewModel.getVehicleListError().observe(this, Observer<String> {
            progressBar.gone()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment)
            .addToBackStack(null).commit()
    }


    fun getViewModel(): CoxViewModel {
        return coxViewModel
    }

    override fun onDestroy() {
        coxViewModel.disposeElements()
        super.onDestroy()
    }

}

