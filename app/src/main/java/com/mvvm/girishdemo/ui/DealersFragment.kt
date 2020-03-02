package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.girishdemo.R
import com.mvvm.girishdemo.base.BaseFragment
import com.mvvm.girishdemo.model.Dealer
import com.mvvm.girishdemo.ui.recyclerView.CoxRecyclerView
import com.mvvm.girishdemo.ui.recyclerView.DealerListAdapter
import com.mvvm.girishdemo.ui.recyclerView.ItemDecorator
import com.mvvm.girishdemo.ui.recyclerView.OnItemClickListener
import com.mvvm.girishdemo.utils.Constants
import com.mvvm.girishdemo.utils.Utils
import com.mvvm.girishdemo.utils.gone
import com.mvvm.girishdemo.utils.visible
import kotlinx.android.synthetic.main.activity_main.progressBar
import javax.inject.Inject


/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

class DealersFragment @Inject constructor(
    private val utils: Utils
) : BaseFragment(), OnItemClickListener {

    private var sharedPreference: SharedPreferences? = null
    private var dealersRecyclerView: CoxRecyclerView? = null
    private var itemDecorator: ItemDecorator? = null
    lateinit var coxViewModel: CoxViewModel
    private var dealerAdapter =
        DealerListAdapter(this)

    override fun getLayoutResId(): Int = R.layout.dealers_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = context?.getSharedPreferences("cox_prefs", Context.MODE_PRIVATE)
        coxViewModel = (activity as MainActivity).getViewModel()
        setupRecyclerView()
        getDealerList()
        (activity as MainActivity).setTitle(R.string.dealers_title)
        itemDecorator = context?.let { ItemDecorator(it) }
    }

    private fun setupRecyclerView() {
        dealersRecyclerView = view?.findViewById(R.id.dealersList) as CoxRecyclerView
        dealersRecyclerView?.apply {
            adapter = dealerAdapter
            isHorizontal = false
            itemDecorator?.let { addItemDecoration(it) }
        }
    }

    //first time make api call to get dealer info
    private fun getDealerList() {
        if (!utils.isDataBaseCreated()) {
            progressBar.visible()
            coxViewModel.getDealerList()
            coxViewModel.getDealerListResult().observe(viewLifecycleOwner, Observer<List<Dealer>> {
                Log.d("DealersFragment: From API", it.toString())
                Toast.makeText(context, "All Dealers retrieved from Server", Toast.LENGTH_SHORT)
                    .show()
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
            Toast.makeText(context, "All Dealers fetched from Database", Toast.LENGTH_SHORT).show()
            dealerAdapter.apply {
                dealers = it
                notifyDataSetChanged()
            }
        })
    }

    override fun onItemClicked(dealerId: Int) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, VehiclesFragment(dealerId), null)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setTitle(R.string.cox_demo)
    }
}