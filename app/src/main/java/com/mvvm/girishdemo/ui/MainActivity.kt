package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
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

class MainActivity : BaseActivity(true) {

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
            Toast.makeText(this, it, Toast.LENGTH_LONG)
        })
    }

    private fun setupButtonOnClick() {
        get_dealers.setOnClickListener {
            //start dealer fragment
            loadDealerFragment()
        }
    }

    private fun loadDealerFragment() {
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, DealersFragment(utils))
            .addToBackStack("root").commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val fm = supportFragmentManager
                if (fm.backStackEntryCount > 0) {
                    fm.popBackStack()
                    return true
                } else {
                    actionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getViewModel(): CoxViewModel {
        return coxViewModel
    }

    override fun onDestroy() {
        coxViewModel.disposeElements()
        super.onDestroy()
    }

}

