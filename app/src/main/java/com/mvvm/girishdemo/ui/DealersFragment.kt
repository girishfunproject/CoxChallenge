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
import com.mvvm.girishdemo.utils.Constants
import com.mvvm.girishdemo.utils.Utils
import com.mvvm.girishdemo.utils.gone
import com.mvvm.girishdemo.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

class DealersFragment @Inject constructor(
    private val utils: Utils
) : BaseFragment() {

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

    //first time make api call to get dealer info
    private fun getDealerList() {
        if (!utils.isDataBaseCreated()) {
            progressBar.visible()
            coxViewModel.getDealerList()
            coxViewModel.getDealerListResult().observe(viewLifecycleOwner, Observer<List<Dealer>> {
                Log.d("DealersFragment: From API", it.toString())
                //set a flag to shared prefs that Vehicle and Dealer tables are successfully created
                sharedPreference?.edit()?.putBoolean(Constants.IS_DATABASE_CREATED, true)?.apply()
                coxViewModel.getDealerListFromDB()
            })
        } else {
            progressBar.visible()
            coxViewModel.getDealerListFromDB()
        }

        coxViewModel.getDealerListDBResult().observe(viewLifecycleOwner, Observer<List<Dealer>> {
            progressBar.gone()
            Log.d("DealersFragment: From DATABASE", it.toString())
            //populate recycler view here
        })
    }
}