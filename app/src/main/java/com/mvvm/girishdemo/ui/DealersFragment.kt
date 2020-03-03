package com.mvvm.girishdemo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
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
import kotlinx.android.synthetic.main.dealers_fragment.*
import javax.inject.Inject


/**
 * Created by Girish Sigicherla on 2/26/2020.
 */

/**
 * A fragment class used to populate list of dealers that are fetched from the @see[Dealer] dealers table.
 * The dealer list is populated on a custom recycler view @see[CoxRecyclerView] that can display items either horizontally or vertically.
 * We set a flag using shared preferences in this class once we retrieve all dealers from the server and storing them in the database
 */
class DealersFragment @Inject constructor(
    private val utils: Utils
) : BaseFragment(), OnItemClickListener {

    private var sharedPreference: SharedPreferences? = null
    private var dealersRecyclerView: CoxRecyclerView? = null
    private var itemDecorator: ItemDecorator? = null
    private lateinit var coxViewModel: CoxViewModel
    private var dealerAdapter =
        DealerListAdapter(this)

    override fun getLayoutResId(): Int = R.layout.dealers_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setTitle(R.string.dealers_title)
        sharedPreference = context?.getSharedPreferences("cox_prefs", Context.MODE_PRIVATE)
        coxViewModel = (activity as MainActivity).getViewModel()
        itemDecorator = context?.let { ItemDecorator(it) }
        setupRecyclerView()
        getDealerList()
    }

    /**
     * To set up a recycler view to show list of dealers of type @see[CoxRecyclerView]
     * To demonstrate the flexibility of our custom recycler view, i'm setting it in default vertical mode.
     */
    private fun setupRecyclerView() {
        dealersRecyclerView = view?.findViewById(R.id.dealersList) as CoxRecyclerView
        dealersRecyclerView?.apply {
            adapter = dealerAdapter
            isHorizontal = false
            itemDecorator?.let { addItemDecoration(it) }
        }
    }

    /**
     * First time make api call to get dealer info for all dealers. As we make an API call and get the response we insert dealers to the database.
     * We observe the live data object to get a notification when this is complete and we populate the dealer list fetched from the database
     * on to the recycler view.
     *
     * NOTE: All all the dealers are inserted into the database, this means that we have successfully saved all vehicles and dealers to the respective tables.
     * At this point we set a flag in the @see[sharedPreference] that database is created.
     *
     * For all subsequent calls we fetch data directly from the database by relying on the @see[utils] isDataBaseCreated method
     */
    private fun getDealerList() {
        if (!utils.isDataBaseCreated()) {
            progress_bar.visible()
            coxViewModel.getDealerList()
            coxViewModel.getDealerListResult().observe(viewLifecycleOwner, Observer<List<Dealer>> {
                Log.d("$TAG : From API", it.toString())
                Toast.makeText(context, "All Dealers retrieved from Server", Toast.LENGTH_SHORT)
                    .show()
                //set a flag to shared prefs that Vehicle and Dealer tables are successfully created
                sharedPreference?.edit()?.putBoolean(Constants.IS_DATABASE_CREATED, true)?.apply()
                coxViewModel.getDealerListFromDB()
            })
        } else {
            progress_bar.visible()
            coxViewModel.getDealerListFromDB()
        }

        coxViewModel.getDealerListDBResult().observe(viewLifecycleOwner, Observer<List<Dealer>> {
            progress_bar.gone()
            Log.d("$TAG : From DATABASE", it.toString())
            Toast.makeText(context, getString(R.string.dealers_fetched_msg), Toast.LENGTH_SHORT).show()
            dealerAdapter.apply {
                dealers = it
                notifyDataSetChanged()
            }
        })
    }

    /**
     * To launch @see[VehiclesFragment] to show a list of vehicles for the given dealerId
     */
    override fun onItemClicked(dealerId: Int) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.fragmentContainer, VehiclesFragment(dealerId), null)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setTitle(R.string.cox_demo)
    }

    private companion object {
        private val TAG = DealersFragment::class.java.simpleName
    }
}