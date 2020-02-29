package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseActivity
import com.mvvm.girishdemo.model.Vehicle
import com.mvvm.girishdemo.utils.visible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(true) {

    override fun getLayoutResId(): Int = R.layout.activity_main

    @Inject
    lateinit var coxViewModelFactory: CoxViewModelFactory
    lateinit var coxViewModel: CoxViewModel

    private var sharedPreference: SharedPreferences? = null
    private var list: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupButtonOnClick()
        coxViewModel =
            ViewModelProviders.of(this, coxViewModelFactory).get(CoxViewModel::class.java)
        sharedPreference = getSharedPreferences("cox_prefs", Context.MODE_PRIVATE)
        getVehicleList()
    }

    private fun getVehicleList() {
        coxViewModel.getVehicleList()

        //call to get all vehicles info and then we enable the Get Dealers button
        coxViewModel.getVehicleListResult().observe(this, Observer<List<Vehicle>> {
            Log.d("MainActivity:", it.toString())
            get_dealers.visible()
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
        fragmentTransaction.replace(R.id.fragmentContainer, DealersFragment())
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

}

