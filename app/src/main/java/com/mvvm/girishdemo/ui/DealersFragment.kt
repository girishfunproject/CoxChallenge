package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseFragment
import com.mvvm.girishdemo.model.Dealer


/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

class DealersFragment : BaseFragment() {

    private var sharedPreference: SharedPreferences? = null
    lateinit var coxViewModel: CoxViewModel

    override fun getLayoutResId(): Int = R.layout.dealers_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreference = context?.getSharedPreferences("cox_prefs", Context.MODE_PRIVATE)
        coxViewModel = (activity as MainActivity).getViewModel()
        //setuprecyclerView()
        getDealerList()
    }

    //first time make api call to get vehicle infos
    private fun getDealerList() {

        coxViewModel.getDealerList()
        coxViewModel.getDealerListResult().observe(viewLifecycleOwner, Observer<List<Dealer>> {
            Log.d("DealersFragment:", it.toString())
        })
    }
}